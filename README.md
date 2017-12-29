## androidXmlRtlSupport ##

This simple repository is written to help developers who want to add RTL support to their old android XML layout files.

Do not expect this code to do magic for you but if you have got a lot of XML layouts containing marginLeft, marginRight, padingLeft, paddingRight, etc. or gravity=left, right, etc. you can ask this code to add marginStart, marginEnd, etc. to your XML files :)

Also this code won't replace start/end based attributes if you've already added some of them :D


----------


## Usage ##

## **BACKUP YOUR FILES!!!** ##

set input/output root in Main.java file. If you've designed your layouts in a way that they look like RTL, this code has to use Start for Right instead of Left and vice versa. So you can tell code if your layouts are like that or not.

    public static final String rootOfXmls = "inputDir";
    public static final String rootOfXmlsOutput = "outputDir";
    public static final boolean isLayoutDesignedInRtl = false;

On the other hand if you want to exclude some directories/files you can exclude them by changing the code of shouldProcessFile function inside LayoutXMLFileProcessor class.

This repository is not complete yet so it does not change your java file (e.g. change Gravity.LEFT set to views in java code) so if you have codes like that, you have to change it yourself. following function is what I use for them currently:

    public static int gravity(int g){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            boolean isLeftToRight = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_LTR;
            if(isLeftToRight){
                switch (g){
                    case Gravity.RIGHT:
                        return Gravity.END;
                    case Gravity.LEFT:
                        return Gravity.START;
                }
            }
            else{
                switch (g){
                    case Gravity.RIGHT:
                        return Gravity.START;
                    case Gravity.LEFT:
                        return Gravity.END;
                }
            }
        }
        return g;
    }

