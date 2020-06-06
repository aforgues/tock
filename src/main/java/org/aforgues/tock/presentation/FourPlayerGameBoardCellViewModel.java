package org.aforgues.tock.presentation;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.aforgues.tock.domain.HoleType;

@NoArgsConstructor
@Data
public class FourPlayerGameBoardCellViewModel {
    private boolean hasHole;
    private boolean hasEmptyHole;
    private String holePawnColor;
    private boolean isPawnStake;
    private int holePawnPlayerNumber;
    private boolean isPawnPlayable;
    private boolean isCenter;
    private int imageCenterNumber;
    private String holePositionCode;

    public String getPawnPlayerTitle() {
        if (! hasEmptyHole) {
            return "Pawn number " + holePawnPlayerNumber
                    + (isPawnPlayable ? " (playable)" : "")
                    + " - position " + getHolePositionCode();
        }
        return "";
    }

    public String getPawnCssClass() {
        if (! hasEmptyHole && holePawnColor != null) {
            return "dot "
                    + holePawnColor.toLowerCase()
                    + (isPawnStake ? " stake" : "");
        }
        return "";
    }

    public String getTargetPawnPosition() {
        if (holePositionCode != null && holePositionCode.startsWith(HoleType.REGULAR.name())) {
            return holePositionCode.substring(holePositionCode.lastIndexOf("-")+1);
        }
        return "";
    }

    public String getPawnIdentifier() {
        if (holePositionCode != null) {
            return holePawnColor + "-" + holePawnPlayerNumber;
        }
        return "";
    }

    public void computeImageCenterNumber(int row, int column) {
        switch (row) {
            case 11:
                switch (column) {
                    case 11:
                        this.imageCenterNumber = 1;
                        break;
                    case 12:
                        this.imageCenterNumber = 2;
                        break;
                    case 13:
                        this.imageCenterNumber = 3;
                        break;
                }
                break;
            case 12:
                switch (column) {
                    case 11:
                        this.imageCenterNumber = 4;
                        break;
                    case 12:
                        this.imageCenterNumber = 5;
                        break;
                    case 13:
                        this.imageCenterNumber = 6;
                        break;
                }
                break;
            case 13:
                switch (column) {
                    case 11:
                        this.imageCenterNumber = 7;
                        break;
                    case 12:
                        this.imageCenterNumber = 8;
                        break;
                    case 13:
                        this.imageCenterNumber = 9;
                        break;
                }
                break;
        }
    }

    public String getCenterImageSource() {
        if (isCenter) {
            return "/images/holecenter-" + this.imageCenterNumber + ".png";
        }
        return "";
    }
}
