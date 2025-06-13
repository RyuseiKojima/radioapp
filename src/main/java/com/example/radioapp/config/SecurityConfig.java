package com.example.radioapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 *  Spring Securityの設定
 */
@SuppressWarnings("unused")
@Configuration // 設定クラスであることを示す
@EnableWebSecurity
public class SecurityConfig {
    /**
     * フィルタ処理
     * @param http HttpSecurityクラス
     * @return SecurityFilterChain
     * @throws Exception 例外処理
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // 開発用DBコンソールの例外的な許可
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // HTTPヘッダーの X-Frame-Options を無効化
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/programs").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll() // ログイン画面へのアクセス許可
                        .successHandler(customAuthenticationSuccessHandler()) // 遷移先のカスタムハンドラ
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );

        /*
         * HttpSecurityオブジェクトを生成し、オブジェクトもしくはnullを返す。
         * build()メソッドはHttpSecurityBuilderインターフェースのメソッド。
         * HttpSecurityBuilderは、
         * HttpSecurity、WebSecurityなどの実装クラス
         * HttpSecurityは、DefaultSecurityFilterChainインターフェースを実装しており、
         * DefaultSecurityFilterChainはSecurityFilterChainを実装しているため、ポリモーフィズム的にも正しい。
         */
        return http.build(); // SecurityFilterChain を構築し、Spring Security に登録
    }

    /**
     * ログイン時遷移先のカスタムハンドラ
     * @return AuthenticationSuccessHandler
     */
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            /**
             * 認証成功時に自動呼び出し
             * @param request リクエスト情報（ログインフォームなど）
             * @param response レスポンス情報（リダイレクト処理に使う）
             * @param authentication 認証済みユーザーの情報（ユーザー名・ロールなど）
             * @throws IOException 入出力(IO)操作中に発生する例外
             */
            @Override
            public void onAuthenticationSuccess(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication) throws IOException {

                // ロールを確認して遷移先を決定
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    if (authority.getAuthority().equals("ROLE_ADMIN")) {
                        response.sendRedirect("/admin/programs");
                        return;
                    }
                }

                // 一般ユーザーは通常ページへ
                response.sendRedirect("/programs");
            }
        };
    }

    /**
     * パスワードを安全にハッシュ化するためのエンコーダ
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}