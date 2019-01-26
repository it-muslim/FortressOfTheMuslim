package jmapps.fortressofthemuslim.Model;

import jmapps.fortressofthemuslim.R;

public class ListAppM {

    private final int iconApp;
    private final String strNameApp;
    private final String strLinkApp;

    public static final ListAppM[] listAppM = {
            new ListAppM(R.drawable.notes_small, "Заметки",
                    "https://play.google.com/store/apps/details?id=jmapps.simplenotes"),
            new ListAppM(R.drawable.supplications_small, "Мольбы из Корана",
                    "https://play.google.com/store/apps/details?id=jmapps.supplicationsfromquran"),
            new ListAppM(R.drawable.arabicinyourhands, "Арабский в твоих руках",
                    "https://play.google.com/store/apps/details?id=jmapps.arabicinyourhands"),
            new ListAppM(R.drawable.lessons_small, "Уроки Рамадана",
                    "https://play.google.com/store/apps/details?id=jmapps.lessonsoframadan"),
            new ListAppM(R.drawable.fortress_small, "Крепость мусульманина",
                    "https://play.google.com/store/apps/details?id=jmapps.fortressofthemuslim"),
            new ListAppM(R.drawable.thenames_small, "Толкование прекрасных имён Аллаха",
                    "https://play.google.com/store/apps/details?id=jmapps.thenamesof"),
            new ListAppM(R.drawable.strength_small, "Сила воли",
                    "https://play.google.com/store/apps/details?id=jmapps.strengthofwill"),
            new ListAppM(R.drawable.questions_small, "200 вопросов по вероучению Ислама",
                    "https://play.google.com/store/apps/details?id=jmapps.questions200"),
            new ListAppM(R.drawable.hadith_small, "40 хадисов имама ан-Навави",
                    "https://play.google.com/store/apps/details?id=jmapps.hadith40"),

    };

    private ListAppM(int iconApp, String strNameApp, String strLinkApp) {
        this.iconApp = iconApp;
        this.strNameApp = strNameApp;
        this.strLinkApp = strLinkApp;
    }

    public int getIconApp() {
        return iconApp;
    }

    public String getStrNameApp() {
        return strNameApp;
    }

    public String getStrLinkApp() {
        return strLinkApp;
    }
}