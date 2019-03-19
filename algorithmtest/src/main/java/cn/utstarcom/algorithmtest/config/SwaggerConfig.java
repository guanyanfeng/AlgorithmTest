/**
 * created on 2018年8月27日 下午12:26:08
 */
package cn.utstarcom.algorithmtest.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author UTSC0167
 * @date 2018年8月27日
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .paths(postPaths()).build();
    }

    private Predicate<String> postPaths() {
        return or(regex("/api/v1/.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("algorithmtest API")
                .description("algorithmtest API reference for developers").contact(contact())
                .license("algorithmtest License").licenseUrl("yd@utstarom.cn").version("1.0").build();
    }

    private Contact contact() {

        return new Contact("优地网络", "http://www.utstarcom.cn/", "yd@utstarom.cn");
    }
}
