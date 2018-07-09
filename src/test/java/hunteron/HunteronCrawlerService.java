package hunteron;

import com.github.vector4wang.service.CrawlerService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IDEA
 * User: vector
 * Data: 2018/2/6 0006
 * Time: 11:31
 * Description:
 */
public class HunteronCrawlerService extends CrawlerService<HunteronJob> {


    @Override
    public void parsePage(Document doc, HunteronJob pageObj) {
        List<String> jobTag = new ArrayList<>();
        Elements tags = Jsoup.parse(pageObj.getRequirementsTag()).select("span");
        for (Element tag : tags) {
            jobTag.add(tag.text());
        }
        pageObj.setRequirementsTag(StringUtils.join(jobTag,";"));
        Elements bashTag = Jsoup.parse(pageObj.getDepart()).select("li");
        for (Element element : bashTag) {
            String tmp = element.text();
            if (tmp.startsWith("所属部门：")) {
                pageObj.setDepart(tmp.replace("所属部门：", ""));
            } else if (tmp.startsWith("汇报对象：")) {
                pageObj.setReport(tmp.replace("汇报对象：", ""));
            } else if (tmp.startsWith("招聘人数：")) {
                pageObj.setNumberOfRecruits(tmp.replace("招聘人数：", ""));
            } else if (tmp.startsWith("下属团队：")) {
                pageObj.setSubTeam(tmp.replace("下属团队：", ""));
            }
        }
        Elements companyTag = Jsoup.parse(pageObj.getField()).select("li");
        for (Element element : companyTag) {
            String temp = element.text();
            if (temp.startsWith("行业")) {
                pageObj.setField(temp.substring(2,temp.length()));
            } else if (temp.startsWith("规模")) {
                pageObj.setScale(temp.substring(2,temp.length()));
            }else if (temp.startsWith("阶段")) {
                pageObj.setFinancing(temp.substring(2,temp.length()));
            }else if (temp.startsWith("性质")) {
                pageObj.setNature(temp.substring(2,temp.length()));
            }else if (temp.startsWith("主页")) {
                pageObj.setCompanyUrl(temp.substring(2,temp.length()));
            }
        }

        Elements desc = Jsoup.parse(pageObj.getJobDesc()).getAllElements();

        Elements seductionOfPositionsTag = Jsoup.parse(pageObj.getSeductionOfPosition()).select("span");
        List<String> sps = new ArrayList<>();
        for (Element seductionOfPosition : seductionOfPositionsTag) {
            sps.add(seductionOfPosition.text());
        }
        String seductionOfPosition = StringUtils.join(sps, ";");
        pageObj.setSeductionOfPosition(seductionOfPosition);

        pageObj.setJobDesc(desc.get(0).text());
        pageObj.setRequirements(desc.get(1).text());
        pageObj.setUrl(doc.location());

    }

	@Override
	public void save(HunteronJob pageObj) {
		System.out.println(pageObj);
	}
}
