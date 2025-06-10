package com.example.radioapp.config;

import com.example.radioapp.domain.RadioStation;
import com.example.radioapp.repository.RadioStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * 共通の設定やエラーハンドリングなどをまとめる
 */
@SuppressWarnings("unused")
@ControllerAdvice // アプリケーション全体に対する設定
@RequiredArgsConstructor
public class BinderConfig {

    private final RadioStationRepository stationRepository;

    /**
     * ラジオ番組の選択チェック（不正値にはnullを返す）
     * @param binder WebDataBinder, エディタの登録に使用
     */
    @InitBinder // リクエストバインディング前に呼び出す
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(RadioStation.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    Long id = Long.valueOf(text);
                    RadioStation station = stationRepository.findById(id)
                            .orElse(null); // 不正なID → null
                    setValue(station);
                } catch (NumberFormatException e) {
                    setValue(null); // "abc" など → null
                }
            }
        });
    }
}
