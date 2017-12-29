package net.androidcart.xmlrtlsupport.utils;

import java.io.File;

/**
 * Created by Amin Amini on 12/29/17.
 */
public class Utils {

    public static void iteratePath( final String path, final IFileProcessor processor ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            String filePath = f.getAbsolutePath();
            if ( f.isDirectory() ) {
                iteratePath( filePath, processor );
            }
            else if (processor.shouldProcessFile(filePath)) {
                try {
                    processor.process(filePath);
                }catch (Exception ignored){}
            }
        }
    }
}
