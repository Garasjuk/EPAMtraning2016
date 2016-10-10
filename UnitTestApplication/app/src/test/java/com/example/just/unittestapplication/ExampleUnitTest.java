package com.example.just.unittestapplication;

import android.view.View;
import android.widget.Button;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    @Mock
    private MainActivity mainActivity;
    private Button btn2;

    @Before
    public void init() {
        System.err.println("init");
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        btn2 = (Button) mainActivity.findViewById(R.id.btn2);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testButtonVisibilityTrue() {
        Mockito.when(mainActivity.getStatus()).thenReturn(true);
        mainActivity.setVisibilityBtn2(View.VISIBLE);
        assertEquals(btn2.getVisibility(), Button.VISIBLE);
    }

    @Test
    public void testButtonVisibilityFalse() {
        Mockito.when(mainActivity.getStatus()).thenReturn(true);
        mainActivity.setVisibilityBtn2(View.VISIBLE);
        assertEquals(btn2.getVisibility(), Button.VISIBLE);
    }
}