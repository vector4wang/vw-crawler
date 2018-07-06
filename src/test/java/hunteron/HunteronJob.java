package hunteron;

import com.github.vector4wang.annotation.CssSelector;
import com.github.vector4wang.util.SelectType;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
public class HunteronJob {

    private long id;

    @CssSelector(selector = "body > div.container.cfix > div.detail-main > div.detail-cont > div.entrust-btn-warp > h2 > span.title.fl",resultType = SelectType.TEXT)
    private String jobName;

    private boolean isHeadHunter; // 是否为猎头

    @CssSelector(selector = "body > div.container.cfix > div.detail-main > div.detail-cont > div.job-title > div > em",resultType = SelectType.TEXT)
    private String salary;

    @CssSelector(selector = "body > div.container.cfix > div.detail-main > div.detail-cont > div.job-title > div > div.j-tags > span")
    private String requirementsTag;

    @CssSelector(selector = "dd.j-blist > ul > li")
    private String depart;
    private String report;
    private String numberOfRecruits;
    private String subTeam;

    @CssSelector(selector = "body > div.container.cfix > div.detail-side > div > div.s-head > p > a",resultType = SelectType.TEXT)
    private String companyName;
    @CssSelector(selector = "body > div.container.cfix > div.detail-side > div > div.s-info > ul > li")
    private String field; // 领域
    private String financing; // 融资情况
    private String scale; // 公司规模
    private String nature; // 性质
    private String companyUrl;

    @CssSelector(selector = "dd.j-detail > p")
    private String jobDesc;
    private String requirements;

    @CssSelector(selector = "body > div.container.cfix > div.detail-main > div.detail-cont > div.detail-items > div:nth-child(1) > dl > dd > span")
    private String seductionOfPosition; // 职位诱惑

    @CssSelector(selector = "body > div.container.cfix > div.detail-main > div.detail-cont > div.detail-items > div.job-item.job-item-nor > dl > dd > p",resultType = SelectType.TEXT)
    private String detailAddress;


    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public boolean isHeadHunter() {
        return isHeadHunter;
    }

    public void setHeadHunter(boolean headHunter) {
        isHeadHunter = headHunter;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRequirementsTag() {
        return requirementsTag;
    }

    public void setRequirementsTag(String requirementsTag) {
        this.requirementsTag = requirementsTag;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getNumberOfRecruits() {
        return numberOfRecruits;
    }

    public void setNumberOfRecruits(String numberOfRecruits) {
        this.numberOfRecruits = numberOfRecruits;
    }

    public String getSubTeam() {
        return subTeam;
    }

    public void setSubTeam(String subTeam) {
        this.subTeam = subTeam;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFinancing() {
        return financing;
    }

    public void setFinancing(String financing) {
        this.financing = financing;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getSeductionOfPosition() {
        return seductionOfPosition;
    }

    public void setSeductionOfPosition(String seductionOfPosition) {
        this.seductionOfPosition = seductionOfPosition;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HunteronJob{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", isHeadHunter=" + isHeadHunter +
                ", salary='" + salary + '\'' +
                ", requirementsTag='" + requirementsTag + '\'' +
                ", depart='" + depart + '\'' +
                ", report='" + report + '\'' +
                ", numberOfRecruits='" + numberOfRecruits + '\'' +
                ", subTeam='" + subTeam + '\'' +
                ", companyName='" + companyName + '\'' +
                ", field='" + field + '\'' +
                ", financing='" + financing + '\'' +
                ", scale='" + scale + '\'' +
                ", nature='" + nature + '\'' +
                ", companyUrl='" + companyUrl + '\'' +
                ", jobDesc='" + jobDesc + '\'' +
                ", requirements='" + requirements + '\'' +
                ", seductionOfPosition='" + seductionOfPosition + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
