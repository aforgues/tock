package org.aforgues.tock.presentation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

public class FourPlayerGameBoardRowViewModel {
    @Getter
    private Collection<FourPlayerGameBoardCellViewModel> cells;

    public FourPlayerGameBoardRowViewModel() {
        cells = new ArrayList<>();
    }

    public void addCell(FourPlayerGameBoardCellViewModel fourPlayerGameBoardCellViewModel) {
        cells.add(fourPlayerGameBoardCellViewModel);
    }
}
