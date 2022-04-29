package ca.gc.hc.siteDemo.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import ca.canada.ised.wet.cdts.components.wet.interceptor.WETLocaleChangeInterceptor;
import ca.canada.ised.wet.cdts.components.wet.interceptor.WETTemplateInterceptor;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@EnableWebMvc
@Configuration
@ComponentScan("ca.canada.ised.wet.cdts")
public class WebConfig implements WebMvcConfigurer {
  /** The cdn template interceptor. */
  @Autowired
  private WETTemplateInterceptor cdnTemplateInterceptor;

  // adds a template interceptor for the thymeleaf components defined in CDTS
  /** {@inheritDoc} */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
    registry.addInterceptor(cdnTemplateInterceptor);
  }

  // allows us to serve static assets
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String[] STATIC_RESOURCE = {"/", "classpath:/", "classpath:/META-INF/resources/",
        "classpath:/resources/", "classpath:/static/", "classpath:/public/"};

    if (!registry.hasMappingForPattern("/**")) {
      registry.addResourceHandler("/**").addResourceLocations(STATIC_RESOURCE);
    }
  }

  /**
   * LocaleChangeInterceptor.
   *
   * @return <code>LocaleChangeInterceptor</code>
   */
  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    WETLocaleChangeInterceptor lci = new WETLocaleChangeInterceptor();
    lci.setParamName("lang");
    return lci;
  }

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.CANADA);
    return slr;
  }

  @Bean
  public LayoutDialect layoutDialect() {
    return new LayoutDialect();
  }
}
