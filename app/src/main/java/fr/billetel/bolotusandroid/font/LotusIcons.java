package fr.billetel.bolotusandroid.font;

import com.joanzapata.iconify.Icon;

public enum LotusIcons implements Icon {
    lotus_application('a');

  char character;

  LotusIcons(char character) {
    this.character = character;
  }

  @Override
  public String key() {
    return name().replace('_', '-');
  }

  @Override
  public char character() {
    return character;
  }
}
