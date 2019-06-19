package store;

import javafx.scene.control.Label;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class SessionTimer {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    private LocalDateTime current = LocalDateTime.MIN;

    private Timer timer = new Timer("Session");
    private TimerTask timerTask;

    public SessionTimer(Label currentSession, Label lastSession) {
        this.timerTask = new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    currentSession.setText(dtf.format(current));
                    current = current.plus(1, ChronoUnit.SECONDS);
                });
            }
        };
        setUpLastSessionTime(lastSession);
    }

    private void setUpLastSessionTime(Label lastSession) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(".session")));
            javafx.application.Platform.runLater(() -> lastSession.setText(content));
        } catch (IOException ignored) {
        }
    }

    public void start() {
        timer.scheduleAtFixedRate(this.timerTask, 0, 1000);
    }

    public void stop() {
        try {
            Files.write(Paths.get(".session"), dtf.format(current).getBytes());
        } catch (IOException ignored) {}
        this.timerTask.cancel();
    }
}
