/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shenjitang.common.util;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.shenjitang.common.StringNumber;

/**
 *
 * @author xiaolie33
 */
public class StringUtilExTest {
    
    public StringUtilExTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of simpleTempleEval method, of class StringUtilEx.
     */
    @Test
    public void testSimpleTempleEval() {
        assertEquals("123456", StringUtilEx.simpleTempleEval("12#456", '#', 3));
        assertEquals("123456", StringUtilEx.simpleTempleEval("#2#456", '#', 1, 3));
        assertEquals("123456", StringUtilEx.simpleTempleEval("12#45#", '#', 3, 6));
        assertEquals("123456", StringUtilEx.simpleTempleEval("12##56", '#', 3,4));
        assertEquals("你好啊傻傻地说", StringUtilEx.simpleTempleEval("你好啊#地说", '#', "傻傻"));
    }
    
}
