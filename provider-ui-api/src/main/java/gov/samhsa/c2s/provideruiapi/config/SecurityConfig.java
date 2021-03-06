package gov.samhsa.c2s.provideruiapi.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import static gov.samhsa.c2s.common.oauth2.OAuth2ScopeUtils.hasScopes;

@Configuration
public class SecurityConfig {

    private static final String RESOURCE_ID = "providerUiApi";

    @Bean
    public ResourceServerConfigurer resourceServer(SecurityProperties securityProperties) {
        return new ResourceServerConfigurerAdapter() {
            @Override
            public void configure(ResourceServerSecurityConfigurer resources) {
                resources.resourceId(RESOURCE_ID);
            }

            @Override
            public void configure(HttpSecurity http) throws Exception {
                if (securityProperties.isRequireSsl()) {
                    http.requiresChannel().anyRequest().requiresSecure();
                }
                http.authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/config/**").access(hasScopes("providerUiApi.read"))

                        .antMatchers(HttpMethod.GET, "/pcm/**").access(hasScopes("providerUiApi.read"))
                        .antMatchers(HttpMethod.POST, "/pcm/**").access(hasScopes("providerUiApi.write"))
                        .antMatchers(HttpMethod.DELETE, "/pcm/**").access(hasScopes("providerUiApi.write"))
                        .antMatchers(HttpMethod.PUT, "/pcm/**").access(hasScopes("providerUiApi.write"))

                        .antMatchers(HttpMethod.GET, "/pls/**").access(hasScopes("providerUiApi.read"))

                        .antMatchers(HttpMethod.POST, "/pep/access").access(hasScopes("providerUiApi.write"))

                        .antMatchers(HttpMethod.GET, "/vss/**").access(hasScopes("providerUiApi.read"))

                        .antMatchers(HttpMethod.GET, "/try-policy/**").access(hasScopes("providerUiApi.read"))

                        .antMatchers(HttpMethod.GET, "/ums/users/**").access(hasScopes("providerUiApi.read"))
                        .antMatchers(HttpMethod.POST, "/ums/users/**").access(hasScopes("providerUiApi.write"))
                        .antMatchers(HttpMethod.DELETE, "/ums/users/**").access(hasScopes("providerUiApi.write"))
                        .antMatchers(HttpMethod.PUT, "/ums/users/**").access(hasScopes("providerUiApi.write"))
                        .antMatchers(HttpMethod.OPTIONS, "/ums/users/**").access(hasScopes("providerUiApi.write"))
                        .antMatchers(HttpMethod.GET, "/ums/providers/profile/**").access(hasScopes("providerUiApi.read"))

                        .antMatchers(HttpMethod.POST, "/uaa/login").permitAll()
                        .antMatchers(HttpMethod.GET, "/ums/userCreationLookupInfo/**").permitAll()
                        .anyRequest().denyAll();
            }
        };
    }
}