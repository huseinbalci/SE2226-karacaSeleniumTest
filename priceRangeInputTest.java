import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class priceRangeInputTest {
    static seleniumBot testBot;

    @BeforeAll
    static void initialize() {
        String url = "https://www.karaca.com";
        testBot = new seleniumBot(url);
        testBot.run();
    }

    @ParameterizedTest
    @CsvSource({
            "0, 1000",
            "1000, 5000",
            "5000, 10000"
    })
    void integerValue(String minPrice, String maxPrice) {
        testBot.searchItem(minPrice, maxPrice);
        assertEquals(minPrice+"-"+maxPrice, testBot.priceRangeSearched());
        testBot.waitBetweenActions();
    }

    @ParameterizedTest
    @CsvSource({
            "ABC, 1000",
            "def, 1000",
            "*%&#, 1000"
    })
    void nonNumericMinimumValue(String minPrice, String maxPrice) {
        testBot.searchItem(minPrice, maxPrice);
        assertTrue(testBot.itemNotFound());
    }

    @Test
    void uppercaseLetterValue() {
        testBot.searchItem("1000", "XYZ");
        assertEquals("1000-XYZ", testBot.priceRangeSearched());
    }

    @Test
    void lowercaseLetterValue() {
        testBot.searchItem("1000", "şğç");
        assertEquals("1000-şğç", testBot.priceRangeSearched());
    }

    @Test
    void symbolValue() {
        testBot.searchItem("1000", "!'^+?");
        assertEquals("1000-!'^+?", testBot.priceRangeSearched());
    }

    @ParameterizedTest
    @CsvSource({
            "ABC, XYZ",
            "def, şğç",
            "*%&#, !'^+?"
    })
    void nonNumericMaxValue(String minPrice, String maxPrice) {
        testBot.searchItem(minPrice, maxPrice);
        assertTrue(testBot.priceRangeVisible());
    }

    @Test
    void minValueIsGreaterThanMaxValue() {
        testBot.searchItem("2000", "1000");
        assertTrue(testBot.itemNotFound());
    }

    @ParameterizedTest
    @CsvSource({
            "1000A*c, 2000",
            "1000, 2000A*c"
    })
    void nonNumericSuffixValue(String minPrice, String maxPrice) {
        testBot.searchItem(minPrice, maxPrice);
        assertTrue(testBot.itemNotFound());
    }

    @ParameterizedTest
    @CsvSource({
            "*Xe1000, 2000",
            "1000, 2000*Xe"
    })
    void nonNumericPrefixValue(String minPrice, String maxPrice) {
        testBot.searchItem(minPrice, maxPrice);
        assertTrue(testBot.itemNotFound());
    }

    @ParameterizedTest
    @CsvSource({
            "95A}i10, 1000",
            "95, 1000A}i10"
    })
    void nonNumericMiddleValue(String minPrice, String maxPrice) {
        testBot.searchItem(minPrice, maxPrice);
        assertTrue(testBot.itemNotFound());
    }

    @Test
    void negativeMinValue() {
        testBot.searchItem("-111111", "0");
        assertTrue(testBot.itemNotFound());
    }

    @Test
    void negativeMaxValue() {
        testBot.searchItem("0", "-111111");
        assertEquals(true, testBot.itemNotFound());
    }

    @Test
    void negativeMinAndMaxValue() {
        testBot.searchItem("-500", "-1500");
        assertTrue(testBot.itemNotFound());
    }

    @Test
    void belowLBMinValue() {
        testBot.searchItem("0", "");
        assertEquals("0-30000", testBot.priceRangeSearched());
    }

    @Test
    void LBMinValue() {
        testBot.searchItem("1", "");
        assertEquals("1-30000", testBot.priceRangeSearched());
    }

    @Test
    void aboveLBMinValue() {
        testBot.searchItem("2", "");
        assertEquals("2-30000", testBot.priceRangeSearched());
    }

    @Test
    void belowUBMinValue() {
        testBot.searchItem("29999", "");
        assertEquals("29999-30000", testBot.priceRangeSearched());
    }

    @Test
    void UBMinValue() {
        testBot.searchItem("30000", "");
        assertEquals("30000-30000", testBot.priceRangeSearched());
    }

    @Test
    void aboveUBMinValue() {
        testBot.searchItem("30001", "");
        assertTrue(testBot.itemNotFound());
    }

    @Test
    void belowLBMaxValue() {
        testBot.searchItem("", "-1");
        assertTrue(testBot.itemNotFound());
    }

    @Test
    void LBMaxValue() {
        testBot.searchItem("", "0");
        assertEquals("0.0-0.0", testBot.priceRangeSearched());
    }

    @Test
    void aboveLBMaxValue() {
        testBot.searchItem("", "1");
        assertEquals("0-1", testBot.priceRangeSearched());
    }

    @Test
    void belowUBMaxValue() {
        testBot.searchItem("", "999999999999999934462");
        assertEquals("0-999999999999999900000", testBot.priceRangeSearched());
    }

    @Test
    void UBMaxValue() {
        testBot.searchItem("", "999999999999999934463");
        assertEquals("0-999999999999999900000", testBot.priceRangeSearched());
    }

    @Test
    void aboveUBMaxValue() {
        testBot.searchItem("", "999999999999999934464");
        assertEquals("0-999999999999999900000", testBot.priceRangeSearched());

    }
}