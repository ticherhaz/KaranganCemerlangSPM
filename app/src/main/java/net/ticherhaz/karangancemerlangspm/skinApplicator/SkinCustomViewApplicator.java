package net.ticherhaz.karangancemerlangspm.skinApplicator;

import android.content.res.TypedArray;
import android.graphics.Color;

import com.zxy.skin.sdk.applicator.SkinViewApplicator;

import net.ticherhaz.karangancemerlangspm.widget.CustomView;

public class SkinCustomViewApplicator extends SkinViewApplicator {

    public SkinCustomViewApplicator() {
        super();
        addAttributeApplicator("lineColor", new IAttributeApplicator<CustomView>() {
            @Override
            public void onApply(CustomView view, TypedArray typedArray, int typedArrayIndex) {
                view.setLineColor(typedArray.getColor(typedArrayIndex, Color.BLACK));
            }
        });
    }
}
