package com.web.blog.controller.packaging;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.web.blog.dao.packaging.PackDao;
import com.web.blog.dao.packaging.PurchaseDao;
import com.web.blog.dao.post.LikeListDao;
import com.web.blog.dao.post.PostListDao;
import com.web.blog.model.packaging.Pack;
import com.web.blog.model.packaging.Purchase;
import com.web.blog.model.post.LikeList;
import com.web.blog.model.post.PostList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    PostListDao postDao;

    @Autowired
    PackDao packDao;

    @Autowired
    PurchaseDao purchaseDao;

    @Autowired
    LikeListDao likeDao;

    @GetMapping("/regist/{packPost}/{email}/{sum}")
    @ApiOperation("패키징 상품 구매")
    public Object regist(@PathVariable List<String> packPost, @PathVariable String email, @PathVariable int sum)
            throws SQLException, IOException {
        try {

            Pack pack = new Pack();
            pack.setEmail(email);
            pack.setPrice(sum);
            packDao.save(pack);
            ///// 패키지테이블에 저장/////

            int packno = pack.getPackno();
            List<String> pidlist = new LinkedList<>();
            pidlist = packPost;
            for (String s : pidlist) {
                int pid = Integer.parseInt(s);
                PostList post = postDao.findByPid(pid);

                Purchase purchase = new Purchase();
                purchase.setEmail(email);
                purchase.setPid(pid);
                purchase.setPackno(packno);
                purchase.setPuse(0);
                char[] charSet = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
                StringBuffer serialno = new StringBuffer();
                for (int i = 0; i < 8; i++) {
                    int idx = (int) (charSet.length * Math.random());
                    serialno.append(charSet[idx]);
                }
                purchase.setSerialno(serialno.toString());
                purchase.setSeller(post.getEmail());
                purchase.setTitle(post.getTitle());
                purchase.setSdate(post.getSdate());
                purchase.setEdate(post.getEdate());
                purchase.setLocation(post.getLocation());
                purchase.setPrice(post.getPrice());
                purchase.setImg(post.getImgurl());
                purchaseDao.save(purchase);
                ///// 구매테이블에 저장/////

                LikeList like = likeDao.findByEmailAndPidAndCart(email, pid, 1);
                likeDao.delete(like);
                ///// 장바구니테이블에서 삭제/////
            }
            return new ResponseEntity<>(pack, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/registOne/{pid}/{email}/{price}")
    @ApiOperation("단일 상품 구매")
    public Object registOne(@PathVariable int pid, @PathVariable String email, @PathVariable int price)
            throws SQLException, IOException {
        try {

            Pack pack = new Pack();
            pack.setEmail(email);
            pack.setPrice(price);
            packDao.save(pack);
            ///// 패키지테이블에 저장/////

            int packno = pack.getPackno();
            PostList post = postDao.findByPid(pid);

            Purchase purchase = new Purchase();
            purchase.setEmail(email);
            purchase.setPid(pid);
            purchase.setPackno(packno);
            purchase.setPuse(0);
            char[] charSet = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'I',
                    'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
            StringBuffer serialno = new StringBuffer();
            for (int i = 0; i < 8; i++) {
                int idx = (int) (charSet.length * Math.random());
                serialno.append(charSet[idx]);
            }
            purchase.setSerialno(serialno.toString());
            purchase.setSeller(post.getEmail());
            purchase.setTitle(post.getTitle());
            purchase.setSdate(post.getSdate());
            purchase.setEdate(post.getEdate());
            purchase.setLocation(post.getLocation());
            purchase.setPrice(post.getPrice());
            purchase.setImg(post.getImgurl());
            purchaseDao.save(purchase);
            ///// 구매테이블에 저장/////
            return new ResponseEntity<>(pack, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/{email}/{page}")
    @ApiOperation("사용자 구매 리스트")
    public Object purchaseList(@PathVariable String email, @PathVariable int page) throws SQLException, IOException {
        try {
            List<Pack> packlist = packDao.findByEmail(email);
            List<List<Purchase>> purlist = new LinkedList<>();
            for (Pack pack : packlist) {
                int packno = pack.getPackno();
                List<Purchase> list = purchaseDao.findByPackno(packno);
                purlist.add(list);
            }

            int start = (page - 1) * 5;
            int end = start + 5;
            if (end > purlist.size()) {
                end = purlist.size();
            }

            List<List<Purchase>> nlist = new LinkedList<>();

            for (int i = start; i < end; i++) {
                nlist.add(purlist.get(i));
            }

            return new ResponseEntity<>(nlist, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count/list/{email}")
    @ApiOperation("사용자 구매 리스트 카운트")
    public Object countPurchaseList(@PathVariable String email) throws SQLException, IOException {
        try {
            List<Pack> packlist = packDao.findByEmail(email);
            List<List<Purchase>> purlist = new LinkedList<>();
            for (Pack pack : packlist) {
                int packno = pack.getPackno();
                List<Purchase> list = purchaseDao.findByPackno(packno);
                purlist.add(list);
            }
            return new ResponseEntity<>(purlist.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sellList/{seller}/{page}")
    @ApiOperation("사업자 판매 리스트")
    public Object sellList(@PathVariable String seller, @PathVariable int page) throws SQLException, IOException {
        try {
            List<Purchase> selllist = purchaseDao.findBySeller(seller, PageRequest.of(page - 1, 10));
            return new ResponseEntity<>(selllist, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count/sellList/{seller}")
    @ApiOperation("사업자 판매 리스트 카운트")
    public Object countSellList(@PathVariable String seller) throws SQLException, IOException {
        try {
            List<Purchase> selllist = purchaseDao.findBySeller(seller);
            return new ResponseEntity<>(selllist.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/checkuse/{purid}/{serialno}")
    @ApiOperation("사용 확정 처리")
    public Object checkuse(@PathVariable int purid, @PathVariable String serialno) throws SQLException, IOException {
        Purchase purchase = purchaseDao.findByPurid(purid);
        String pur_serialno = purchase.getSerialno();
        if (pur_serialno.equals(serialno)) {
            purchase.setPuse(1);
            purchaseDao.save(purchase);
            return new ResponseEntity<>(purchase, HttpStatus.ACCEPTED);

        } else {
            return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        }
    }

}