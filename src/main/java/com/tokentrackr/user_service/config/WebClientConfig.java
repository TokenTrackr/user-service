//package com.tokentrackr.user_service.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.*;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.*;
//import org.springframework.security.oauth2.client.web.reactive.function.client.*;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Configuration
//public class WebClientConfig {
//
//    @Bean
//    public WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
//        var oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
//        oauth2.setDefaultClientRegistrationId("keycloak");
//
//        return WebClient.builder()
//                .apply(oauth2.oauth2Configuration())
//                .build();
//    }
//
//    @Bean
//    public OAuth2AuthorizedClientManager authorizedClientManager(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientRepository authorizedClientRepository
//    ) {
//        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(
//                clientRegistrationRepository,
//                new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository)
//        );
//    }
//}
