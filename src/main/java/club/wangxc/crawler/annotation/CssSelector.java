package club.wangxc.crawler.annotation;

import club.wangxc.crawler.util.SelectType;

import java.lang.annotation.*;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/12/14
 * Time: 9:27
 * Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CssSelector {

    /**
     * css 选择器
     * @return
     */
    String selector() default "";

    /**
     * 匹配结果，默认为HTML代码
     * @return
     */
    SelectType resultType() default SelectType.HTML;

}
