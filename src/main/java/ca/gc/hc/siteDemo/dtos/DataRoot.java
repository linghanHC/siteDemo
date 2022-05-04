package ca.gc.hc.siteDemo.dtos;

import java.util.List;
import lombok.Data;

@Data
public class DataRoot<E> {
	public List<E> data;
}