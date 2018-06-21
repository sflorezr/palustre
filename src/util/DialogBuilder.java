/*
 * Proyecto Robot FX Propiedad de CHOUCAIR CARDENAS TESTING S. A.
 * el presente proyecto fue iniciativa del equipo de Migracion - BI
 * agradecimiento es pecial al colaborador Jaider Adriam Serrano Sepulveda.
 * Medellin - Colombia 2016.
 */
package util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 *
* @author Jaider Adriam Serrano Sepulveda
 */
public class DialogBuilder {

/**
 * DialogBuilder compatible version with 8u45. This class allows to create a dialog that can be displayed to user in
 * order to show some information.
 *
 * <p>
 * <b>Required to create the dialog:</b>
 * <ul>
 * <li>Indicate the dialog type that has to be built:<br>
 * This can be achieved by calling any of these methods (it is required to invoke one of them):
 * <ol>
 * <li>{@link #alert(AlertType)} builds an alert to show some information.</li>
 * <li>{@link #textInput()} builds a dialog with a text-field for input.</li>
 * <li>{@link #choice(Object, List)} builds a dialog with a combo box.</li>
 * </ol>
 * The order in which the builder evaluates which type of dialog is the same they were enumerated previously.</li>
 *
 * <li>Set title, header or content text; these are optional and can be used based on the need.
 * <li>It is possible to show an exception stack trace {@link #exception(Throwable)}.
 * <li>It is possible to show HTML text using {@link #detailsText(String)}. If the text passed has HTML tags then a
 * WebView is used to show the text, otherwise a regular TextArea is used.
 * </ul>
 *
 * @author Jaider Adriam Serrano Sepulveda - 04.09.2016.
 */
  private static Window defaultOwner;

  private String title;
  private String headerText;
  private String contentText;
  private String detailsText;
  private Window owner;
  private AlertType alertType;
  private List<ButtonType> buttonTypes;
  private EMintDialogType dialogType;
  private Object choiceSelectedItem;
  private List<Object> choiceAvailableItems;
  private Throwable detailException;
  private Modality modality;
  private Image image;

  private DialogBuilder() {
    super();
  }

  /**
   * Sets a default owner which will be used from now on when creating new dialogs using this builder.
   *
   * @param _defaultOwner Sets the default window owner.
   */
  public static void setDefaultOwner(Window _defaultOwner) {
    defaultOwner = _defaultOwner;
  }

  /**
   * Creates a new instance of the dialog builder class.
   *
   * @return A new instance of this class.
   */
  public static DialogBuilder create() {
    return new DialogBuilder();
  }

  /**
   * Sets the dialog title text. If no title is provided, then this builder sets a default title based on the dialog
   * type that will be created; in case the dialog is an Alert, then it is used the AlertType passed.
   *
   * @param _title The title for the dialog that will be created.
   * @return the current DialogBuilder instance.
   */
  public DialogBuilder title(String _title) {
    this.title = _title;
    return this;
  }

  /**
   * Sets the dialog header text, if this text is not passed no {@code masthead} is shown.
   *
   * @param _headerText Header Text in dialog.
   * @return The current DialogBuilder instance.
   */

  public DialogBuilder headerText(String _headerText) {
    this.headerText = _headerText;
    return this;
  }

  /**
   * Sets the dialog content text, the text right above the buttons.
   *
   * @param _contentText Text into the dialog body.
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder contentText(String _contentText) {
    this.contentText = _contentText;
    return this;
  }

  /**
   * Sets a customized image in the dialog that will be created. In case the {@link #headerText(String)} is set, the
   * icon will be shown in the {@code masthead} section at the right side; if the text is not set, then no
   * {@code masthead} will be shown and then the icon will be visible at the left side together the
   * {@link DialogBuilder#contentText(String)}.
   *
   * This will replace the default image when using {@link #alert(AlertType)} dialogs.
   *
   * @param _image Icon for the showed in header dialog.
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder image(Image _image) {
    this.image = _image;
    return this;
  }

  /**
   * Sets a text that is shown as hidden details, the user has to click on a button to see the text.
   *
   * @param _detailsText Details of dialog information.
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder detailsText(String _detailsText) {
    this.detailsText = _detailsText;
    return this;
  }

  /**
   * Sets the owner of dialog.
   *
   * @param _owner New window owner.
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder owner(Window _owner) {
    this.owner = _owner;
    return this;
  }

  /**
   * Indicates to the builder that an Alert instance has to be created.
   *
   * @param _alertType Alert type of dialog. (ERROR, INFORMATION, WARNING, CONFIRMATION, NONE)
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder alert(AlertType _alertType) {
    this.dialogType = EMintDialogType.ALERT;
    this.alertType = Objects.requireNonNull(_alertType);
    return this;
  }

  /**
   * Sets the buttons that have to be shown in the dialog.
   *
   * @param _buttonTypes Button types list to show in dialog.
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder buttons(ButtonType... _buttonTypes) {
    this.buttonTypes = Lists.newArrayList(_buttonTypes);
    return this;
  }

  /**
   * Indicates to the builder that a TextInput instance has to be created.
   *
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder textInput() {
    this.dialogType = EMintDialogType.TEXT_INPUT;
    return this;
  }

  /**
   * Indicates to the builder that a {@link ChoiceDialog} instance has to be created and must be returned when calling
   * {@link #build()}.
   *
   * @param <T> Object type for the choice list.
   *
   * @param _choiceSelectedItem Item Selected for default
   * @param _choiceAvailableItems Items list
   * @return The current DialogBuilder instance.
   */
  public <T extends Object> DialogBuilder choice(T _choiceSelectedItem, List<T> _choiceAvailableItems) {
    dialogType = EMintDialogType.CHOICE;
    choiceSelectedItem = _choiceSelectedItem;
    choiceAvailableItems = (List<Object>) _choiceAvailableItems;
    return this;
  }

  /**
   * Indicates to the builder that a {@link MintSessionTimeOutDialog} will be created and returned when calling
   * {@link #build()}.
   *
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder sessionTimeout() {
    dialogType = EMintDialogType.SESSION_TIMEOUT;
    headerText = "Tiempo de Sesion Terminado";
    return this;
  }

  /**
   * Indicates to the builder that a {@link MintPasswordInputDialog} will be created and returned when calling
   * {@link #build()}.
   *
   * This dialog sets by default two button types: {@link ButtonTypes#LOGIN}.
   *
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder passwordInput() {
    this.dialogType = EMintDialogType.PASSWORD_INPUT;
    return this;
  }

  /**
   * Allows to show the stack trace of an exception. This method works in contrast with {@link #detailsText(String)}, if
   * you call both, then only the details text will be displayed.
   *
   * @param _exceptionThrowable exception Throwable
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder exception(Throwable _exceptionThrowable) {
    this.detailException = _exceptionThrowable;
    return this;
  }

  /**
   * Sets the modality of the dialog.
   *
   * @param _modality Configute dialog modal mode.
   * @return The current DialogBuilder instance.
   */
  public DialogBuilder modality(Modality _modality) {
    this.modality = _modality;
    return this;
  }

  /**
   * This method builds the Dialog with all the data collected by the methods in this builder.
   *
   * @param <T> Object type Inherited it of Dialog class.
   *
   * @return Build dialog
   * @throws IllegalStateException When no dialog type has been specified.
   * @see #alert(AlertType)
   * @see #textInput()
   * @see #choice(Object, List)
   */
  public <T extends Dialog<?>> T build() {
    Preconditions.checkNotNull(dialogType);

    Dialog<?> dialog = createDialog();
    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
    stage.getIcons().add(new Image(Util.parametros.get("logo")));

    dialog.setTitle(generateDialogTitle());
    dialog.setHeaderText(headerText);
    dialog.setContentText(contentText);

    DialogPane dialogPane = dialog.getDialogPane();
    dialogPane.setExpandableContent(generateExpandableContent());

    // Modality
    if (modality != null) {
      dialog.initModality(modality);
    }

    // Image
    if (image != null) {
      dialogPane.setGraphic(new ImageView(image));
    }

    // Owner and Icon
    if (owner != null) {
      // Icons inherited from owner
      dialog.initOwner(owner);
    }
    else if (defaultOwner != null) {
      // Icons inherited from defaultOwner
      dialog.initOwner(defaultOwner);
    }
    else {
      // Set default icon
      stage = (Stage) dialog.getDialogPane()
          .getScene()
          .getWindow();

      //stage.getIcons().addAll(MintImage.getWALogos());
    }

    return (T) dialog;
  }

  private Dialog<?> createDialog() {
    Dialog<?> dialog;

    switch (dialogType) {
      case ALERT:
        dialog = dialogTypeAlert();
        break;

      case TEXT_INPUT:
        dialog = dialogTypeTextInput();
        break;

      case CHOICE:
        dialog = dialogTypeChoise();
        break;

      default:
        throw new UnsupportedOperationException("Dialog Type not supported yet!");
    }

    return dialog;
  }

  private Node generateExpandableContent() {
    if (detailException != null) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      //The printStackTrace use is intentional
      detailException.printStackTrace(printWriter);//NOSONAR
      String exceptionText = stringWriter.toString();

      Label detailLbl = new Label("Detalles de la excepción:");

      TextArea textArea = new TextArea(exceptionText);
      textArea.setEditable(false);
      textArea.setWrapText(true);

      textArea.setMaxWidth(Double.MAX_VALUE);
      textArea.setMaxHeight(Double.MAX_VALUE);
      GridPane.setVgrow(textArea, Priority.ALWAYS);
      GridPane.setHgrow(textArea, Priority.ALWAYS);

      GridPane expContent = new GridPane();
      expContent.setMaxWidth(Double.MAX_VALUE);
      expContent.add(detailLbl, 0, 0);
      expContent.add(textArea, 0, 1);

      return expContent;
    }

    if (detailsText != null) {
      Node content;

      if (detailsText.contains("<html>") || detailsText.contains("<br>") || detailsText.contains("<b>") || detailsText.contains("</p>")) {
        WebView webView = new WebView();
        webView.setContextMenuEnabled(false);
        WebEngine engine = webView.getEngine();
        engine.loadContent(detailsText);
        webView.setMaxWidth(Double.MAX_VALUE);
        webView.setMaxHeight(Double.MAX_VALUE);

        StackPane stackPane = new StackPane(webView);
        stackPane.setStyle("-fx-border-color: #bababa");
        stackPane.setPrefSize(400, 170);
        stackPane.setMaxWidth(Double.MAX_VALUE);
        stackPane.setMaxHeight(Double.MAX_VALUE);

        content = stackPane;
      }
      else {
        TextArea textArea = new TextArea(detailsText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        content = textArea;
      }

      GridPane.setVgrow(content, Priority.ALWAYS);
      GridPane.setHgrow(content, Priority.ALWAYS);

      GridPane root = new GridPane();
      root.setMaxWidth(Double.MAX_VALUE);
      root.add(content, 0, 0);

      return root;
    }

    return null;
  }

  private Dialog<?> dialogTypeAlert() {
    Alert alert = new Alert(alertType);

    if (buttonTypes == null || buttonTypes.isEmpty()) {
      if (null != alertType) switch (alertType) {
            case NONE:
                buttonTypes = Lists.newArrayList();
                break;
            case CONFIRMATION:
                buttonTypes = Lists.newArrayList(ButtonTypes.OK, ButtonTypes.CANCEL);
                break;
            default:
                buttonTypes = Lists.newArrayList(ButtonTypes.OK);
                break;
        }
    }

    Dialog<?> dialog = alert;
    dialog.getDialogPane()
        .getButtonTypes()
        .setAll(buttonTypes);

    return dialog;
  }

  private Dialog<?> dialogTypeTextInput() {
    if (buttonTypes == null || buttonTypes.isEmpty()) {
      buttonTypes = Lists.newArrayList(ButtonTypes.OK, ButtonTypes.CANCEL);
    }

    Dialog<?> dialog = new TextInputDialog();
    dialog.getDialogPane()
        .getButtonTypes()
        .setAll(buttonTypes);

    return dialog;
  }

  private Dialog<?> dialogTypeChoise() {
    if (buttonTypes == null || buttonTypes.isEmpty()) {
      buttonTypes = Lists.newArrayList(ButtonTypes.OK, ButtonTypes.CANCEL);
    }

    Dialog<?> dialog = new ChoiceDialog<>(choiceSelectedItem, choiceAvailableItems);
    dialog.getDialogPane()
        .getButtonTypes()
        .setAll(buttonTypes);

    return dialog;
  }

  private String generateDialogTitle() {
    String result = title;

    if (result == null) {
      result = titleDialogType();
      if (result == null) {
        result = titleAlertType();
      }
    }

    return result;
  }

  private String titleDialogType() {
    String result = null;

    if (dialogType == EMintDialogType.CHOICE) {
      result = "¡Seleccione Item!";
    }
    else if (dialogType == EMintDialogType.TEXT_INPUT) {
      result = "¡Digite Dato Por Favor!";
    }
    else if (dialogType == EMintDialogType.SESSION_TIMEOUT) {
      result = "¡Tiempo de Sesion Alcanzado!";
    }
    else if (alertType == AlertType.ERROR) {
      result = "¡Error!";
    }

    return result;
  }

  private String titleAlertType() {
    String result = null;

    if (alertType == AlertType.WARNING) {
      result = "¡Advertencia!";
    }
    else if (alertType == AlertType.CONFIRMATION) {
      result = "¡Confirme por favor!";
    }

    return result == null ? "¡Informacion!" : result;
  }

  /**
   * Enumerate using for the identify the dialog type to build.
   *
   * @author Jaider Serrano, 26.01.2016.
   */
  private enum EMintDialogType {
    /**
     * Alert dialog.
     */
    ALERT,
    /**
     * Text input dialog.
     */
    TEXT_INPUT,
    /**
     * Choice object dialog.
     */
    CHOICE,
    /**
     * Session Timeout dialog.
     */
    SESSION_TIMEOUT,
    /**
     * Password dialog.
     */
    PASSWORD_INPUT;
  }
}