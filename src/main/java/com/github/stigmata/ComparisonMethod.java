package com.github.stigmata;

/**
 * This enum represents birthmark comparison method. let <it>X</it> be a set of
 * target <it>{ x1, x2, ..., xn, }</it> and <it>Y</it> be a set of target
 * <it>{ y1, y2, ..., ym, }</it>.
 * 
 * @author Haruaki Tamada
 */
public enum ComparisonMethod{
    /**
     * This constants represents comparing all combination between <it>X</it>
     * and <it>Y</it>. <it>x1 - y1, x1 - y2, x1 - y3, ..., xn - ym-1, xn, ym</it>.
     */
    ROUND_ROBIN_XY,
    /**
     * This constants represents comparing all combination between <it>X</it>.
     * <it>x1 - x1, x1 - x2, x1 - x3, ..., xn - xn-1 xn, xn</it>.
     */
    ROUND_ROBIN_SAME_PAIR,
    /**
     * This constants represents comparing all combination between <it>X</it>.
     * <it>x1 - x1, x1 - x2, x1 - x3, ..., xn - xn-1 xn, xn</it>.
     */
    ROUND_ROBIN_WITHOUT_SAME_PAIR,
    /**
     * This constants represents comparing some pairs. The pair is guessed by
     * its name. If x1 and y3 have same name, then the pair of x1 and y3 is
     * compared.
     */
    GUESSED_PAIR,
    /**
     * This constants represents comparing some pairs. The pair is specified by
     * user.
     */
    SPECIFIED_PAIR,
}
