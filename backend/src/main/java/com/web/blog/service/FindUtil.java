package com.web.blog.service;

import java.util.Random;

public class FindUtil {
    

    // 인증코드
    public static String createKey() throws Exception{

        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for(int i = 0; i < 10; i++){
            int index = rnd.nextInt(3);
            switch (index) {
                case 0:
                 key.append((char) ((int)(rnd.nextInt(26))+97));
                    break;
                case 1:
                 key.append((char) ((int)(rnd.nextInt(26))+65));
                case 2:
                 key.append((rnd.nextInt(10)));
                    break;
            }
        }

		return key.toString();
    }
    // 난수로 임시 비밀번호 설정
    public static String getNewPwd() throws Exception {
        char[] charSet ={'0','1','2','3','4','5','6','7','8','9'
                        ,'A','B','C','D','E','F','G','I','J','K','L','M','N','O','P'
                        ,'Q','R','S','T','U','V','W','X','Y','Z','!','@','$','%','^','&','+','=','.'};
        
    StringBuffer newKey = new StringBuffer();
    newKey.append(charSet[10]).append(charSet[0]);
    for(int i = 2; i < 10; i++){
        int idx = (int)(charSet.length*Math.random());
        newKey.append(charSet[idx]);
    }
    return newKey.toString();
  }
}