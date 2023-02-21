package fudan.se.myWardrobe.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HistoryDTO {
    private List<History> history;
}
