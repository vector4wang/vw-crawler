package csdn;

import com.github.vector4wang.annotation.CssSelector;
import com.github.vector4wang.util.SelectType;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/10 0010
 * Time: 17:54
 * Description: 
 */
public class Blog {

	@CssSelector(selector = "#mainBox > main > div.blog-content-box > div.article-title-box > h1",resultType = SelectType.TEXT)
	private String title;

	@CssSelector(selector = "#mainBox > main > div.blog-content-box > div.article-info-box > div > span.time",resultType = SelectType.TEXT)
	private String lastUpdateDate;

	@CssSelector(selector = "#mainBox > main > div.blog-content-box > div.article-info-box > div > div > span",resultType = SelectType.TEXT)
	private int readNum;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public int getReadNum() {
		return readNum;
	}

	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}

	@Override
	public String toString() {
		return "Blog{" + "title='" + title + '\'' + ", lastUpdateDate='"
				+ lastUpdateDate + '\'' + ", readNum=" + readNum + '}';
	}
}
