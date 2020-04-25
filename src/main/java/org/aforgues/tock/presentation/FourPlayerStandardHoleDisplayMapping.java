package org.aforgues.tock.presentation;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum FourPlayerStandardHoleDisplayMapping {
    POS_00(0, 8, 23),
    POS_01(1, 8, 22),
    POS_02(2, 8, 21),
    POS_03(3, 8, 20),
    POS_04(4, 8, 19),
    POS_05(5, 8, 18),
    POS_06(6, 8, 17),
    POS_07(7, 8, 16),
    POS_08(8, 7, 16),
    POS_09(9, 6, 16),
    POS_10(10, 5, 16),
    POS_11(11, 4, 16),
    POS_12(12, 3, 16),
    POS_13(13, 2, 16),
    POS_14(14, 1, 16),
    POS_15(15, 1, 14),
    POS_16(16, 1, 12),
    POS_17(17, 1, 10),
    POS_18(18, 1, 8),
    POS_19(19, 2, 8),
    POS_20(20, 3, 8),
    POS_21(21, 4, 8),
    POS_22(22, 5, 8),
    POS_23(23, 6, 8),
    POS_24(24, 7, 8),
    POS_25(25, 8, 8),
    POS_26(26, 8, 7),
    POS_27(27, 8, 6),
    POS_28(28, 8, 5),
    POS_29(29, 8, 4),
    POS_30(30, 8, 3),
    POS_31(31, 8, 2),
    POS_32(32, 8, 1),
    POS_33(33, 10, 1),
    POS_34(34, 12, 1),
    POS_35(35, 14, 1),
    POS_36(36, 16, 1),
    POS_37(37, 16, 2),
    POS_38(38, 16, 3),
    POS_39(39, 16, 4),
    POS_40(40, 16, 5),
    POS_41(41, 16, 6),
    POS_42(42, 16, 7),
    POS_43(43, 16, 8),
    POS_44(44, 17, 8),
    POS_45(45, 18, 8),
    POS_46(46, 19, 8),
    POS_47(47, 20, 8),
    POS_48(48, 21, 8),
    POS_49(49, 22, 8),
    POS_50(50, 23, 8),
    POS_51(51, 23, 10),
    POS_52(52, 23, 12),
    POS_53(53, 23, 14),
    POS_54(54, 23, 16),
    POS_55(55, 22, 16),
    POS_56(56, 21, 16),
    POS_57(57, 20, 16),
    POS_58(58, 19, 16),
    POS_59(59, 18, 16),
    POS_60(60, 17, 16),
    POS_61(61, 16, 16),
    POS_62(62, 16, 17),
    POS_63(63, 16, 18),
    POS_64(64, 16, 19),
    POS_65(65, 16, 20),
    POS_66(66, 16, 21),
    POS_67(67, 16, 22),
    POS_68(68, 16, 23),
    POS_69(69, 14, 23),
    POS_70(70, 12, 23),
    POS_71(71, 10, 23);

    private int holePosition;
    private int mappedColumnPosition;
    private int mappedRowPosition;

    public static Optional<Integer> positionFromColumnAndRow(final int columnPosition, final int rowPosition) {
        return Arrays.stream(FourPlayerStandardHoleDisplayMapping.values())
                .filter(fourPlayerStandardHoleDisplayMapping -> fourPlayerStandardHoleDisplayMapping.mappedColumnPosition == columnPosition)
                .filter(fourPlayerStandardHoleDisplayMapping -> fourPlayerStandardHoleDisplayMapping.mappedRowPosition == rowPosition)
                .map(fourPlayerStandardHoleDisplayMapping -> fourPlayerStandardHoleDisplayMapping.holePosition)
                .findFirst();
    }
}
