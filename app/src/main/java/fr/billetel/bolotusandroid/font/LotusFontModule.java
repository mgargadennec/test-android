package fr.billetel.bolotusandroid.font;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

public class LotusFontModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        return "lotus_font.ttf";
    }

    @Override
    public Icon[] characters() {
        return LotusIcons.values();
    }
}
