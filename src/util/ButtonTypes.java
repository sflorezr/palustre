/*
 * Proyecto Robot FX Propiedad de CHOUCAIR CARDENAS TESTING S. A.
 * el presente proyecto fue iniciativa del equipo de Migracion - BI
 * agradecimiento es pecial al colaborador Jaider Adriam Serrano Sepulveda.
 * Medellin - Colombia 2016.
 */
package util;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

/**
 * MintButtonType class lets you create different types of buttons required for the dialogs.
 *
 * @author Jaider Adriam Serrano Sepulveda, 04.09.2016.
 */
public class ButtonTypes {
  /**
   * Option button Apply for decision messages.
   */
  public static final ButtonType APPLY = new ButtonType("Aplicar", ButtonData.APPLY);
  /**
   * Option button Close for decision messages.
   */
  public static final ButtonType CLOSE = new ButtonType("Cerrar", ButtonData.CANCEL_CLOSE);
  /**
   * Option button OK for decision messages.
   */
  public static final ButtonType OK = new ButtonType("OK", ButtonData.OK_DONE);
  /**
   * Option button Finish for decision messages.
   */
  public static final ButtonType FINISH = new ButtonType("Finlizar", ButtonData.FINISH);
  /**
   * Option button Yes for decision messages.
   */
  public static final ButtonType YES = new ButtonType("Si", ButtonData.YES);
  /**
   * Option button No for decision messages.
   */
  public static final ButtonType NO = new ButtonType("No", ButtonData.NO);
  /**
   * Option button Next for decision messages.
   */
  public static final ButtonType NEXT = new ButtonType("Siguiente", ButtonData.NEXT_FORWARD);
  /**
   * Option button Previous for decision messages.
   */
  public static final ButtonType PREVIOUS = new ButtonType("Anterior", ButtonData.BACK_PREVIOUS);
  /**
   * Option button Cancel for decision messages.
   */
  public static final ButtonType CANCEL = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
  /**
   * Option button Cancel for decision messages.
   */
  public static final ButtonType EXIT = new ButtonType("Salir", ButtonData.CANCEL_CLOSE);

}
