package com.example.radioapp.service;

import com.example.radioapp.domain.AppUser;
import com.example.radioapp.dto.UserRegistrationForm;
import com.example.radioapp.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * ユーザデータ業務処理
 */
@Service
@SuppressWarnings("unused") // Spring Securityによって自動で呼ばれるため
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserDetailsService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * username に対応するユーザー情報を取得し、UserDetails（≒AppUser）を返す
     * Spring Security がログイン時に必ず呼び出す唯一のメソッド
     * @param username ユーザ名
     * @return ユーザデータ
     * @throws UsernameNotFoundException ユーザ名なし
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DBからユーザーを検索し、見つからなければ例外を投げる
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
    }

    /**
     * ユーザデータを保存
     * @param userRegistrationForm ユーザ登録フォームデータ
     */
    public void save(UserRegistrationForm userRegistrationForm) {
        AppUser user = new AppUser();
        user.setUsername(userRegistrationForm.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationForm.getPassword()));

        appUserRepository.save(user);
    }
}
