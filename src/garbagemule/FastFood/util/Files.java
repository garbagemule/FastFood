package garbagemule.FastFood.util;

import garbagemule.FastFood.FastFood;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Files
{
    public static File extract(String filename, File dir)
    {
        // Skip if file exists
        File file = new File(dir, filename);
        if (file.exists()) return file;
        
        // Skip if there is no resource with that name
        InputStream in = FastFood.class.getResourceAsStream("/res/" + filename);
        if (in == null) return null;
        
        try
        {
            // Set up an output stream
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[8192];
            int length = 0;
            
            // Write the resource data to the file
            while ((length = in.read(buffer)) > 0)
                out.write(buffer, 0, length);
            
            if (in != null)  in.close();
            if (out != null) out.close();
            
            return file;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            FastFood.warning("Problem creating file '" + filename + "'!");
        }
        
        return null;
    }
}
