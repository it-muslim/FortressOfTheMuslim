package jmapps.fortressofthemuslim.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Objects;

import jmapps.fortressofthemuslim.R;

public class Settings extends BottomSheetDialogFragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    public static final String keyTextArSize = "key_text_ar_size";
    public static final String keyTextTrSize = "key_text_tr_size";
    public static final String keyTextRuSize = "key_text_ru_size";

    public static final String keyTrVisibleState = "key_tr_visible_state";
    public static final String keyRuVisibleState = "key_ru_visible_state";

    private static final String keyProgressArabic = "key_progress_arabic";
    public static final String keyRStateArabic = "key_r_arabic";
    public static final String keyGStateArabic = "key_g_arabic";
    public static final String keyBStateArabic = "key_b_arabic";

    private static final String keyProgressTranscription = "key_progress_transcription";
    public static final String keyRStateTranscription = "key_r_transcription";
    public static final String keyGStateTranscription = "key_g_transcription";
    public static final String keyBStateTranscription = "key_b_transcription";

    private static final String keyProgressRussian = "key_progress_russian";
    public static final String keyRStateRussian = "key_r_russian";
    public static final String keyGStateRussian = "key_g_russian";
    public static final String keyBStateRussian = "key_b_russian";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private Button btnTextArSizeM;
    private ToggleButton tbArSetting;
    private Button btnTextArSizeP;
    private SeekBar sbTextColorAr;

    private Button btnTextTrSizeM;
    private ToggleButton tbTrSetting;
    private Button btnTextTrSizeP;
    private SeekBar sbTextColorTr;
    private CheckBox cbTextTr;

    private Button btnTextRuSizeM;
    private ToggleButton tbRuSetting;
    private Button btnTextRuSizeP;
    private SeekBar sbTextColorRu;
    private CheckBox cbTextRu;

    private int textArabicSize;
    private int textTranscriptionSize;
    private int textRussianSize;

    @SuppressLint("CommitPrefEdits")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootSettings = inflater.inflate(R.layout.bottom_sheet_settings, container, false);
        setRetainInstance(true);
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPreferences.edit();

        btnTextArSizeM = rootSettings.findViewById(R.id.btn_text_ar_size_mm);
        tbArSetting = rootSettings.findViewById(R.id.tb_ar_settings);
        btnTextArSizeP = rootSettings.findViewById(R.id.btn_text_ar_size_pp);
        sbTextColorAr = rootSettings.findViewById(R.id.sb_text_color_ar);

        btnTextTrSizeM = rootSettings.findViewById(R.id.btn_text_tr_size_mm);
        tbTrSetting = rootSettings.findViewById(R.id.tb_tr_setting);
        btnTextTrSizeP = rootSettings.findViewById(R.id.btn_text_tr_size_pp);
        sbTextColorTr = rootSettings.findViewById(R.id.sb_text_color_tr);
        cbTextTr = rootSettings.findViewById(R.id.cb_text_tr);

        btnTextRuSizeM = rootSettings.findViewById(R.id.btn_text_ru_size_mm);
        tbRuSetting = rootSettings.findViewById(R.id.tb_ru_setting);
        btnTextRuSizeP = rootSettings.findViewById(R.id.btn_text_ru_size_pp);
        sbTextColorRu = rootSettings.findViewById(R.id.sb_text_color_ru);
        cbTextRu = rootSettings.findViewById(R.id.cb_text_ru);

        textArabicSize = mPreferences.getInt(keyTextArSize, 18);
        textTranscriptionSize = mPreferences.getInt(keyTextTrSize, 18);
        textRussianSize = mPreferences.getInt(keyTextRuSize, 18);

        tbArSetting.setTextSize(textArabicSize);
        tbTrSetting.setTextSize(textTranscriptionSize);
        tbRuSetting.setTextSize(textRussianSize);

        boolean textTrState = mPreferences.getBoolean(keyTrVisibleState, false);
        boolean textRuState = mPreferences.getBoolean(keyRuVisibleState, false);

        cbTextTr.setChecked(textTrState);
        cbTextRu.setChecked(textRuState);

        tbTrSetting.setChecked(textTrState);
        tbRuSetting.setChecked(textRuState);

        int sbArProgress = mPreferences.getInt(keyProgressArabic, 0);
        sbTextColorAr.setProgress(sbArProgress);

        int ARStateAr = mPreferences.getInt(keyRStateArabic, 0);
        int AGStateAr = mPreferences.getInt(keyGStateArabic, 0);
        int ABStateAr = mPreferences.getInt(keyBStateArabic, 0);
        tbArSetting.setTextColor(Color.argb(255, ARStateAr, AGStateAr, ABStateAr));

        int sbTrProgress = mPreferences.getInt(keyProgressTranscription, 0);
        sbTextColorTr.setProgress(sbTrProgress);

        int ARStateTr = mPreferences.getInt(keyRStateTranscription, 0);
        int AGStateTr = mPreferences.getInt(keyGStateTranscription, 0);
        int ABStateTr = mPreferences.getInt(keyBStateTranscription, 0);
        tbTrSetting.setTextColor(Color.argb(255, ARStateTr, AGStateTr, ABStateTr));

        int sbRuProgress = mPreferences.getInt(keyProgressRussian, 0);
        sbTextColorRu.setProgress(sbRuProgress);

        int ARStateRu = mPreferences.getInt(keyRStateRussian, 0);
        int AGStateRu = mPreferences.getInt(keyGStateRussian, 0);
        int ABStateRu = mPreferences.getInt(keyBStateRussian, 0);
        tbRuSetting.setTextColor(Color.argb(255, ARStateRu, AGStateRu, ABStateRu));

        btnTextArSizeM.setOnClickListener(this);
        btnTextArSizeP.setOnClickListener(this);
        sbTextColorAr.setOnSeekBarChangeListener(this);

        btnTextTrSizeM.setOnClickListener(this);
        btnTextTrSizeP.setOnClickListener(this);
        cbTextTr.setOnCheckedChangeListener(this);
        sbTextColorTr.setOnSeekBarChangeListener(this);

        btnTextRuSizeM.setOnClickListener(this);
        btnTextRuSizeP.setOnClickListener(this);
        cbTextRu.setOnCheckedChangeListener(this);
        sbTextColorRu.setOnSeekBarChangeListener(this);

        return rootSettings;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_text_ar_size_mm:
                textArabicSize--;
                textSizeMinus(textArabicSize, keyTextArSize, tbArSetting);

                break;

            case R.id.btn_text_ar_size_pp:
                textArabicSize++;
                textSizePlus(textArabicSize, keyTextArSize, tbArSetting);

                break;

            case R.id.btn_text_tr_size_mm:
                textTranscriptionSize--;
                textSizeMinus(textTranscriptionSize, keyTextTrSize, tbTrSetting);

                break;

            case R.id.btn_text_tr_size_pp:
                textTranscriptionSize++;
                textSizePlus(textTranscriptionSize, keyTextTrSize, tbTrSetting);

                break;

            case R.id.btn_text_ru_size_mm:
                textRussianSize--;
                textSizeMinus(textRussianSize, keyTextRuSize, tbRuSetting);

                break;

            case R.id.btn_text_ru_size_pp:
                textRussianSize++;
                textSizePlus(textRussianSize, keyTextRuSize, tbRuSetting);

                break;
        }

        mEditor.putInt(keyTextArSize, textArabicSize).apply();
        mEditor.putInt(keyTextTrSize, textTranscriptionSize).apply();
        mEditor.putInt(keyTextRuSize, textRussianSize).apply();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_text_tr:
                tbTrSetting.setChecked(isChecked);
                mEditor.putBoolean(keyTrVisibleState, isChecked).apply();
                break;
            case R.id.cb_text_ru:
                tbRuSetting.setChecked(isChecked);
                mEditor.putBoolean(keyRuVisibleState, isChecked).apply();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()) {
            case R.id.sb_text_color_ar:

                int ARStateAr = 0;
                int AGStateAr = 0;
                int ABStateAr = 0;

                textColor(progress, ARStateAr, AGStateAr, ABStateAr, keyRStateArabic, keyGStateArabic,
                        keyBStateArabic, keyProgressArabic, tbArSetting);
                break;

            case R.id.sb_text_color_tr:

                int ARStateTr = 0;
                int AGStateTr = 0;
                int ABStateTr = 0;

                textColor(progress, ARStateTr, AGStateTr, ABStateTr, keyRStateTranscription, keyGStateTranscription,
                        keyBStateTranscription, keyProgressTranscription, tbTrSetting);
                break;

            case R.id.sb_text_color_ru:

                int ARStateRu = 0;
                int AGStateRu = 0;
                int ABStateRu = 0;

                textColor(progress, ARStateRu, AGStateRu, ABStateRu, keyRStateRussian, keyGStateRussian,
                        keyBStateRussian, keyProgressRussian, tbRuSetting);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void textSizeMinus(int textSize, String keyTextSize, ToggleButton tbTextItem) {

        if (textSize >= 8) {
            tbTextItem.setTextSize(textSize);
        } else {
            Toast.makeText(getActivity(), "Это минимальный порог", Toast.LENGTH_SHORT).show();
        }

        mEditor.putInt(keyTextSize, textSize).apply();
    }

    private void textSizePlus(int textSize, String keyTextSize, ToggleButton tbTextItem) {

        if (textSize <= 50) {
            tbTextItem.setTextSize(textSize);
        } else {
            Toast.makeText(getActivity(), "Это максимальный порог", Toast.LENGTH_SHORT).show();
        }

        mEditor.putInt(keyTextSize, textSize).apply();
    }

    private void textColor(int progress,
                           int RState,
                           int GState,
                           int BState,
                           String keyR,
                           String keyG,
                           String keyB,
                           String keyProgress,
                           ToggleButton tbTextItem) {

        if (progress < 256) {
            BState = progress;
        } else if (progress < 256 * 2) {
            GState = progress % 256;
            BState = 256 - progress % 256;
        } else if (progress < 256 * 3) {
            GState = 255;
            BState = progress % 256;
        } else if (progress < 256 * 4) {
            RState = progress % 256;
            GState = 256 - progress % 256;
            BState = 256 - progress % 256;
        } else if (progress < 256 * 5) {
            RState = 255;
            GState = 0;
            BState = progress % 256;
        } else if (progress < 256 * 6) {
            RState = 255;
            GState = progress % 256;
            BState = 256 - progress % 256;
        } else if (progress < 256 * 7) {
            RState = 255;
            GState = 255;
            BState = progress % 256;
        }

        tbTextItem.setTextColor(Color.argb(255, RState, GState, BState));

        mEditor.putInt(keyProgress, progress).apply();
        mEditor.putInt(keyR, RState);
        mEditor.putInt(keyG, GState);
        mEditor.putInt(keyB, BState);
        mEditor.apply();
    }
}