package com.example.myapplication;
import com.example.myapplication.control.LocalFIleUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void test_FileWrite() throws Exception {
        LocalFIleUtil.writeJsonParamsToFile("/Users/bytedance/Desktop/out.txt","{\"c\":\"b\"}");

    }

    public void generateRandom(){

    }
}