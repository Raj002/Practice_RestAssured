package slack;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Period {
    MONTHLY("Monthly"),
    WEEKLY("Weekly"),
    DAILY("Daily");

    private final String period;
}
