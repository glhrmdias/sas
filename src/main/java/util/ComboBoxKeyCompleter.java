package util;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class ComboBoxKeyCompleter {

    final StringBuilder completer = new StringBuilder();
    final Tooltip tooltip;
    Timer timer;

    public ComboBoxKeyCompleter() {
        tooltip = new Tooltip();
        tooltip.setAutoHide(true);
    }

    public <A> void install(ComboBox<A> comboBox) {
        Tooltip controlTooltip = comboBox.getTooltip();

        comboBox.focusedProperty().addListener((obs, ov, nv) -> {
            completer.setLength(0);
            tooltip.setText("");
            tooltip.hide();
            comboBox.setTooltip(controlTooltip);
        });

        comboBox.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                completer.setLength(0);
            } else if (event.getCode() == KeyCode.BACK_SPACE && completer.length() > 0) {
                completer.setLength(completer.length() - 1);
                showTooltip((Stage) comboBox.getScene().getWindow(), comboBox);
            }
        });

        comboBox.addEventFilter(KeyEvent.KEY_TYPED, event -> {

            String valor = event.getCharacter();

            if (!valor.matches("[a-zA-Z0-9. ]")) return;

            completer.append(valor);

            for (A item : comboBox.getItems()) {
                if (item.toString().toLowerCase().contains(completer.toString().toLowerCase())) {
                    comboBox.setValue(item);
                    break;
                }
            }

            showTooltip((Stage) comboBox.getScene().getWindow(), comboBox);
        });
    }

    private void showTooltip(Stage owner, Control control) {
        tooltip.setText(completer.toString());

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(tooltip::hide);
                completer.setLength(0);
                timer.cancel();
            }
        }, 3000);

        if (tooltip.isShowing()) return;

        Point2D p = control.localToScene(0.0, 0.0);

        control.setTooltip(tooltip);

        tooltip.show(owner, p.getX()
                        + control.getScene().getX() + control.getScene().getWindow().getX(),
                p.getY() - control.getHeight() - 10 + control.getScene().getY()
                        + control.getScene().getWindow().getY());
    }

}
