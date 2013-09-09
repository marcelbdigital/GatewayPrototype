/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easit.core.configurations;

import javax.inject.Inject;

import org.easit.core.handlers.ConnectedToHandlerInterceptor;
import org.easit.core.plugins.PluginManager;
import org.easit.dao.LogOperationsDao;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;

/**
 * Web MVC configuration
 *
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Inject
    private LogOperationsDao operationsRepository;

    @Inject
    private PluginManager pluginManager;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(new ConnectedToHandlerInterceptor(operationsRepository, pluginManager));
    }

    @Bean
    public ViewResolver viewResolver() {
	UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
	viewResolver.setViewClass(TilesView.class);
	return viewResolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
	TilesConfigurer configurer = new TilesConfigurer();
	configurer.setDefinitions(new String[] { "/WEB-INF/layouts/tiles.xml", "/WEB-INF/views/**/tiles.xml" });
	configurer.setCheckRefresh(true);
	return configurer;
    }

    @Bean
    public MultipartResolver multipartResolver() {
	return new CommonsMultipartResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public MessageSource messageSource() {
	ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	messageSource.setBasename("/WEB-INF/messages/messages");
	return messageSource;
    }

}
