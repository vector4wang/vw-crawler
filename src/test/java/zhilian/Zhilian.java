package zhilian;

import com.github.vector4wang.annotation.CssSelector;
import com.github.vector4wang.util.SelectType;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/9 0009
 * Time: 11:29
 * Description: 
 */
public class Zhilian {
	@CssSelector(selector = "h1", resultType = SelectType.TEXT)
	private String title = "";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Zhilian{" + "title='" + title + '\'' + '}';
	}
}
