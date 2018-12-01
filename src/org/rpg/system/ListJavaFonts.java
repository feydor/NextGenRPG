package org.rpg.system;

import java.awt.GraphicsEnvironment;

// lists usable fonts on the current os
public class ListJavaFonts
{

  public static void main(String[] args)
  {
    String fonts[] = 
      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    for ( int i = 0; i < fonts.length; i++ )
    {
      System.out.println(fonts[i]);
    }
  }

}
