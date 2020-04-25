package org.aforgues.tock.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.aforgues.tock.domain.HoleType;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum FourPlayerHomeHoleDisplayMapping {

    PLAYER_1_START_POS_01(1, HoleType.HOME_START,1,5,21),
    PLAYER_1_START_POS_02(1, HoleType.HOME_START,2,4,21),
    PLAYER_1_START_POS_03(1, HoleType.HOME_START,3,4,20),
    PLAYER_1_START_POS_04(1, HoleType.HOME_START,4,3,20),
    PLAYER_2_START_POS_01(2, HoleType.HOME_START,1,3,5),
    PLAYER_2_START_POS_02(2, HoleType.HOME_START,2,3,4),
    PLAYER_2_START_POS_03(2, HoleType.HOME_START,3,4,4),
    PLAYER_2_START_POS_04(2, HoleType.HOME_START,4,4,3),
    PLAYER_3_START_POS_01(3, HoleType.HOME_START,1,19,3),
    PLAYER_3_START_POS_02(3, HoleType.HOME_START,2,20,3),
    PLAYER_3_START_POS_03(3, HoleType.HOME_START,3,20,4),
    PLAYER_3_START_POS_04(3, HoleType.HOME_START,4,21,4),
    PLAYER_4_START_POS_01(4, HoleType.HOME_START,1,21,19),
    PLAYER_4_START_POS_02(4, HoleType.HOME_START,2,21,20),
    PLAYER_4_START_POS_03(4, HoleType.HOME_START,3,20,20),
    PLAYER_4_START_POS_04(4, HoleType.HOME_START,4,20,21),
    PLAYER_1_FINISH_POS_01(1, HoleType.HOME_FINISH,1,12,22),
    PLAYER_1_FINISH_POS_02(1, HoleType.HOME_FINISH,2,12,21),
    PLAYER_1_FINISH_POS_03(1, HoleType.HOME_FINISH,3,12,20),
    PLAYER_1_FINISH_POS_04(1, HoleType.HOME_FINISH,4,12,19),
    PLAYER_2_FINISH_POS_01(2, HoleType.HOME_FINISH,1,2,12),
    PLAYER_2_FINISH_POS_02(2, HoleType.HOME_FINISH,2,3,12),
    PLAYER_2_FINISH_POS_03(2, HoleType.HOME_FINISH,3,4,12),
    PLAYER_2_FINISH_POS_04(2, HoleType.HOME_FINISH,4,5,12),
    PLAYER_3_FINISH_POS_01(3, HoleType.HOME_FINISH,1,12,2),
    PLAYER_3_FINISH_POS_02(3, HoleType.HOME_FINISH,2,12,3),
    PLAYER_3_FINISH_POS_03(3, HoleType.HOME_FINISH,3,12,4),
    PLAYER_3_FINISH_POS_04(3, HoleType.HOME_FINISH,4,12,5),
    PLAYER_4_FINISH_POS_01(4, HoleType.HOME_FINISH,1,22,12),
    PLAYER_4_FINISH_POS_02(4, HoleType.HOME_FINISH,2,21,12),
    PLAYER_4_FINISH_POS_03(4, HoleType.HOME_FINISH,3,20,12),
    PLAYER_4_FINISH_POS_04(4, HoleType.HOME_FINISH,4,19,12);

    private int playerRank;
    private HoleType holeType;
    private int holePosition;
    private int mappedColumnPosition;
    private int mappedRowPosition;

    public static Optional<HomeHoleData> positionFromColumnAndRow(final int columnPosition, final int rowPosition) {
        return Arrays.stream(FourPlayerHomeHoleDisplayMapping.values())
                .filter(fourPlayerHomeHoleDisplayMapping -> fourPlayerHomeHoleDisplayMapping.mappedColumnPosition == columnPosition)
                .filter(fourPlayerHomeHoleDisplayMapping -> fourPlayerHomeHoleDisplayMapping.mappedRowPosition == rowPosition)
                .map(fourPlayerHomeHoleDisplayMapping -> new HomeHoleData(fourPlayerHomeHoleDisplayMapping.playerRank,
                                                                          fourPlayerHomeHoleDisplayMapping.holeType,
                                                                          fourPlayerHomeHoleDisplayMapping.holePosition))
                .findFirst();
    }

    @AllArgsConstructor
    @Getter
    public static class HomeHoleData {
        private int playerRank;
        private HoleType holeType;
        private int holePosition;
    }
}
