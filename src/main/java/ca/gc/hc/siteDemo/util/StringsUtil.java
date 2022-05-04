/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ca.gc.hc.siteDemo.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


///**
//	Extensions to {@link String}.
//
//	<ul>
//	<li>possible object sharing: {@link #valueOf(String)}, {@link #valueOf(StringBuffer)}, {@link #valueOf(char)}
//	<li>sorting in dictionary order: {@link #DICTIONARY_ORDER}, {@link #DICTIONARY_CASE_INSENSITIVE_ORDER},
//		{@link #compareDictionary(String, String, boolean)}
//	<li>{@link #trim(String, String)}, {@link #trimWhitespace(String)}, {@link #trimPunct(String)},
//	<li>translate/convert/format: {@link #casify(String, String, Map)}, {@link #toASCII7(String)}, {@link #fromPigLatin(String)},
//		raw {@link #getBytes(String)} without character set encoding
//		<!-- @link #javaString2raw(String)}, @link #raw2javaString(String)}, -->
//	<li>algorithms: {@link #minEditDistance(String, String)}
//	</ul>
//
//	@version $Revision: 1.3.6.1 $ $Date: 2015/08/20 12:36:55 $
// */
public class StringsUtil
{
	public static String PUNCT = ".?!,:;()[]'\"";
	private static Logger log = LoggerFactory.getLogger(StringsUtil.class);

	// list of stop words doesn't have to be complete as -- seems there should be a standard list somewhere

	private static String[] SINGLECHARSTRING = new String[0x100];
	static {
		for (int i = 0, imax = SINGLECHARSTRING.length; i < imax; i++)
			SINGLECHARSTRING[i] = String.valueOf((char) i).intern();
	}

	private StringsUtil()
	{
	}

	/**
    Returns byte array of low byte of each character.
    Like {@link String#getBytes()} but no encoding,
    and {@link String#getBytes(int, int, byte[], int)} but not deprecated.
	 */
	public static byte[] getBytes(String s)
	{
		if (s == null)
			s = "";
		int len = s.length();
		byte[] b = new byte[len];
		for (int i = 0; i < len; i++)
			b[i] = (byte) s.charAt(i);
		return b;
	}

	/**
    Canonicalizes single character strings for which 0<=charAt(0)<=255.
	 */
	public static String valueOf(String str)
	{
		String s;
		if (str == null)
			s = null;
		else if (str.length() == 0)
			s = "";
		else if (str.length() == 1 && str.charAt(0) < SINGLECHARSTRING.length)
			s = SINGLECHARSTRING[str.charAt(0)];
		else
			s = str;
		return s;
	}

	/**
    Return possibly shared String.
    If String is 1-character long and char<256, then guaranteed shared.
	*/
	public static String valueOf(StringBuffer sb)
	{
		String s;
		if (sb == null)
			s = null;
		else if (sb.length() == 0)
			s = "";
		else if (sb.length() == 1 && sb.charAt(0) < SINGLECHARSTRING.length)
			s = SINGLECHARSTRING[sb.charAt(0)];
		else
			s = sb.substring(0); // vs .toString() -- space or speed?
		return s;
	}

	public static String valueOf(char ch)
	{
		return ch < SINGLECHARSTRING.length
		? SINGLECHARSTRING[ch]
		                   : String.valueOf(ch);
	}


	/** Trim letters in passed chars from ends of word. */
	public static String trim(String txt, String chars)
	{
		return trim(txt, chars, 0, txt.length() - 1);
	}
	public static String trim(String txt, String chars, int start, int end)
	{
		//if (txt==null) return null; -- go boom
		while (start < end && chars.indexOf(txt.charAt(start)) != -1)
			start++;
		while (end >= start && chars.indexOf(txt.charAt(end)) != -1)
			end--;
		return (start <= end ? txt.substring(start, end + 1) : "");
	}


	/**
    Returns the minimum number of operations to transform one string into the other.
    An operation is insert character, delete character, substitute character.
	 */
	public static int minEditDistance(String a, String b)
	{
		if (a == b)
			return 0;
		else if (a == null || a.equals(""))
			return b.length();
		else if (b == null || b.equals(""))
			return a.length();
		else if (a.equals(b))
			return 0;

		// dynamic programming
		int alen = a.length() + 1, blen = b.length() + 1;
		int[][] c = new int[alen][blen];

		for (int i = 0; i < alen; i++)
			c[i][0] = i;
		for (int j = 0; j < blen; j++)
			c[0][j] = j;

		for (int i = 1; i < alen; i++)
		{
			for (int j = 1; j < blen; j++)
			{
				int scost =
					c[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);
				int dcost = 1 + c[i - 1][j];
				int icost = 1 + c[i][j - 1];

				c[i][j] = Math.min(scost, Math.min(dcost, icost));
			}
		}

		return c[alen - 1][blen - 1];
	}

	/**
	 * Method trimStringLeft.
	 * Purpose: If substring, exists in string, shorten string by removing characters to the left
	 * of the substring.  If removeSubstring = true, remove substring as well.
	 * @param s
	 * @param subString
	 * @param removeSubstring
	 * @return String
	 */
	public static String trimStringLeft(String s, String subString, boolean removeSubstring) {
		String value= new String("");
		int lastIndex= (s.lastIndexOf(subString));
		if (removeSubstring == true) {
			lastIndex += subString.length();
		}
		value= s.substring(lastIndex, s.length());
		return value;
	}

	/**
	 * @param original A String to check for null
	 * @param substitute A String to substitute for original if the latter is null
	 * @return The original String, or the substitute if the original was null. An empty string if 
	 * both original and substitute were null.
	 * <p>This version of the method, without the Locale parameter, is mostly used where English data
	 * is mandatory, and it is required to be displayed in a French field should the French equivalent 
	 * not be present. </p>
	 * @author Sylvain Larivière 2012-08-15
	 */
	public static String substituteIfNull(String original, String substitute) {
		if (original == null && substitute == null) return "";
		else return (original == null ? substitute : original);			
	}
	
	/**
	 * @param s String s
	 * @return True if the passed String is null or has a zero length; false otherwise.
	 * @author Sylvain Larivière 2012-06-27
	 */
	public static boolean isEmpty( String s) {
		return (s == null || s.trim().length() < 1) || s.trim().equals("");
	}

	/**
	 * @param s String s
	 * @return True if the passed String is not null and has a length greater than zero; false otherwise.
	 * @author Sylvain Larivière 2012-06-27
	 */
	public static boolean hasData( String s) {
		return (s != null && s.trim().length() > 0);
	}
	
	/**
	 * Translate an input string from web application 
     * to upper case, no accents, and replace with double quote 
	 * e.g: input string: Succinate d'acide de d-alpha tocophéryle
	 *      return string: SUCCINATE D"ACIDE DE D-ALPHA TOCOPHERYLE 
	 *      
	 * @param s searech criteria from dpd online application
	 * @return the uppercase, unaccented string
	 */
	public static String AsUnAccentedUppercase(String s) {
		String result= "";
		if (StringsUtil.hasData(s)) {
		    //replace any single quote by a two double quote
			s=s.replace("'","\"");
		    result= (Normalizer.normalize(s, Normalizer.Form.NFD)
		           .replaceAll("[^\\p{ASCII}]", "")).toUpperCase();	
		}	
        log.debug(" AsUnAccentedUppercase input s " + s + " result:" + result);
		return result;
	}

	/**
	 * Query a string content from column s (e.g. table wqry_COMPANIES have column name Company_name) 
	 * Format the string of that column to upper case, no accents, and replace with double quote 
	 * if there is single quote in the string. 
	 * select * from wqry_COMPANIES where Company_name LIKE ... 
	 * to select * from wqry_COMPANIES where
     *    UPPER(CONVERT(REPLACE(Company_name, Chr(39), Chr(34)), 'US7ASCII'))  like 
     *    
	 * @param columnName (name of the column, e.g. Company_name)
	 * @return a string without accents
	 */
	public static String StringWithoutAccent(String columnName) {
		String result= "";
		if (StringsUtil.hasData(columnName)){
		 
			/* Single quotes in SQL will raise an Oracle error. Double
			 * the quotes in criteria items that can include one, for
			 * searching. 
			 * 
			 * UPPER, CONVERT, REPLACE are all Oracle functions
			 * Oracle REPLACE function can only replace Char. 
			 * is single quote, char (34) is double quote.
			 * Oracle CONVERT removes accents of the input 
			 */
			
			// Format the input string columnName as an SQL statement as: 
			//   UPPER(CONVERT(REPLACE(columnName, Chr(39), Chr(34)), 'US7ASCII')) 
			result=" UPPER(CONVERT(REPLACE(" + columnName + ", Chr(39), Chr(34)), 'US7ASCII')) ";
		}
		log.debug (" StringWithoutAccent input columnName: " + columnName + "  output SQL format:" + result);
		return result;
	}
	

    /**
     * @param s
     *            An Object (String or null) to evaluate
     * @return the passed-in String if it is not null, or an empty String
     *         otherwise
     * @author Sylvain Larivière  2013-05-01
     */
    public static String emptyForNull(Object s) {
	return (String) (s == null ? new String("") : s);
    }

}
