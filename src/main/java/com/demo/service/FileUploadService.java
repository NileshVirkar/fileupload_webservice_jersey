package com.demo.service;
 
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;
 
@Path("/hello")
public class FileUploadService {
	public static final int BUF_SIZE = 2 * 1024;
	
	@POST
	@Path("/upload")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public Response saveOrder(@FormDataParam("file") InputStream fileInputStream, @FormDataParam("filename") String filename) throws IOException {
        String output;
        System.out.println("DEstination file name: " + filename);
//		if (fileInputStream.available() > 0) {
            try {
                saveUploadFile(fileInputStream, new File(filename));
                output = "You successfully uploaded " + filename + " into " + filename;
            } catch (Exception e) {
            	output = "You failed to upload " + filename + " => " + e.getMessage();
            }
//        } else {
//        	System.out.println("You failed to upload " + filename + " because the file was empty.");
//        	output = "You failed to upload " + filename + " because the file was empty.";
//        }
		
		return Response.status(200).entity(output).build();
	}
	
	private void saveUploadFile(InputStream input, File dst) throws IOException {
        OutputStream out = null;
        try {
            if (dst.exists()) {
                out = new BufferedOutputStream(new FileOutputStream(dst, true),
                        BUF_SIZE);
            } else {
                out = new BufferedOutputStream(new FileOutputStream(dst),
                        BUF_SIZE);
            }
            byte[] buffer = new byte[BUF_SIZE];
            int len = 0;
            while ((len = input.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Jersey say : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}
 
}