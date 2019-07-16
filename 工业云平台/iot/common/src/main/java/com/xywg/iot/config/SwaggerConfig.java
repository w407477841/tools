package com.xywg.iot.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:31 2018/12/25
 * Modified By : wangyifei
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any()).build();
             //   .globalOperationParameters(setHeaderToken());



    }
    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("iot", "iot", "header"));
        apiKeyList.add(new ApiKey("user", "user", "header"));
        return apiKeyList;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("工业互联网 Swagger API 服务")
                .description("工业互联网 Swagger API 服务")
                .termsOfServiceUrl("http://swagger.io/")
                .contact(new Contact("wyf", "127.0.0.1", "18932224279@163.com"))
                .version("1.0")
                .build();

    }
    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("iot").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        ParameterBuilder  tokenPar1 = new ParameterBuilder();
        tokenPar1.name("user").description("user").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar1.build());
        return pars;
    }


}
