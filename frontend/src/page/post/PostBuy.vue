<template>
  <div>
    <!-- 구매목록이 없을 때 -->
    <div class="mt-5 mb-5" v-if="bitems.length <= 0">
      <i class="fas fa-surprise mt-5 mr-2"></i>
      <span style="font-weight:bold">구매한 상품이 없습니다.</span>
      <i class="fas fa-surprise ml-2"></i>
      <br />
      <button
        @click="goPost"
        class="btn mt-2 mb-4"
        style="font-weight:bold; color:white; background-color:RGB(134, 165, 212); border-radius:7px"
      >
        <i class="fas fa-shopping-cart mr-2"></i>상품 구매하러 가기
      </button>
    </div>

    <!-- 구매목록이 있을때 -->

    <!-- 웹버전 -->
    <div class="Webtable d-none d-sm-block">
      <div class="" v-if="bitems.length > 0">
        <table class="table" v-for="(item, index) in bitems" :key="index">
          <thead class="thead" style="background:RGB(134, 165, 212); color:white;">
            <tr>
              <th>No</th>
              <td>{{ (bpage - 1) * 5 + index + 1 }}</td>
            </tr>
            <tr>
              <th>사진</th>
              <th>상품명</th>
              <th>기간</th>
              <th>가격</th>
              <th>일련번호</th>
              <th>사용여부</th>
            </tr>
          </thead>
          <tbody v-for="(itm, idx) in item" :key="idx">
            <tr id="tt">
              <td>
                <img
                  v-if="itm.img"
                  :src="makeimgurl(itm.img)"
                  @click="goDetail(itm.pid)"
                  style="width: 100px; height: 100px; cursor:pointer;"
                />
              </td>
              <td>
                <div @click="goDetail(itm.pid)" style="cursor:pointer;font-weight:bold;">{{ itm.title }}</div>
              </td>
              <td style="font-weight:bold;">{{ itm.sdate }} ~ {{ itm.edate }}</td>
              <td style="font-weight:bold;">{{ itm.price }}</td>
              <td style="font-weight:bold;">{{ itm.serialno }}</td>
              <td>
                <b-badge v-if="itm.puse == 0" pill variant style="background-color: #003399">미사용</b-badge>
                <b-badge v-if="itm.puse == 1" pill variant style="background-color: #C4302B">사용완료</b-badge>
              </td>
            </tr>
          </tbody>
        </table>
        <br />
        <!-- paging -->
        <b-pagination
          class="mt-5 mb-0"
          v-if="btotalPage > 5"
          v-model="bpage"
          :total-rows="btotalPage"
          pills
          :per-page="5"
        ></b-pagination>
      </div>
    </div>

    <!-- 모바일버전 -->
    <div class="MoblieCard d-block d-sm-none d-md-none">
      <table class="table" v-for="(item, index) in bitems" :key="index">
        <thead class="thead" style="background:RGB(134, 165, 212); color:white;">
          <tr>
            <th>No</th>
            <td>{{ (bpage - 1) * 5 + index + 1 }}</td>
          </tr>
        </thead>
        <div class="container row p-2" @click="goDetail(itm.pid)" style="width:210%" v-for="(itm, idx) in item" :key="idx">
          <div class="col-6 p-2 d-flex align-items-center">
            <img v-if="itm.img" :src="makeimgurl(itm.img)" style="width:100%; heigh:100%;">
            <img v-if="!itm.img" src="../../assets/img/noimage.jpg" style="width:100%; heigh:100%;">
          </div>
          <div class="col-6 p-2 d-flex align-items-center">
            <div class="" style="text-align:left">
              <b-badge v-if="itm.puse == 0" pill variant style="background-color: #003399">미사용</b-badge>
              <b-badge v-if="itm.puse == 1" pill variant style="background-color: #C4302B">사용완료</b-badge>
              <br>
              <div class="mt-1 mb-1">
                <span style="font-size:1rem; font-weight:bold;" class="white-space:pre;">상품명 : [{{ itm.title }}]</span>
              </div>
              <div class="mt-1 mb-1">
                <span style="font-weight:bold;">가격 : {{itm.price}}원</span>
              </div>
              <div class="mt-1 mb-1">
                <span style="font-weight:bold;">일련번호 : {{itm.serialno}}</span>
              </div>
              <div class="mt-1 mb-1">
                <small style="font-weight:bold;">시작일 : {{itm.sdate}}</small>
              </div>
              <div class="mt-1 mb-1">
                <small style="font-weight:bold;">종료일 : {{itm.edate}}</small>
              </div>
            </div>
          </div>
        </div>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  data() {
    return {
      bitems: [],
      bpage: 1,
      btotalPage: 0,
    };
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((res) => {
          this.email = res.data.email;
          this.pageCount();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    pageCount() {
      axios
        .get(`${baseURL}/purchase/count/list/${this.email}`)
        .then((res) => {
          this.btotalPage = res.data;
          this.pageCheck();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    pageCheck() {
      axios
        .get(`${baseURL}/purchase/list/${this.email}/${this.bpage}`)
        .then((res) => {
          this.bitems = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    goDetail(pid) {
      axios
        .get(`${baseURL}/post/detail/${pid}`)
        .then((res) => {
          scroll(0, 0);
          this.$router.push({
            name: "PostListDetail",
            params: { ID: pid },
          });
        })
        .catch((err) => {
          const Toast = Swal.mixin({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            onOpen: (toast) => {
              toast.addEventListener("mouseenter", Swal.stopTimer);
              toast.addEventListener("mouseleave", Swal.resumeTimer);
            },
          });

          Toast.fire({
            icon: "error",
            title: "해당 상품은 삭제된 상품입니다.",
          });
          setTimeout(() => {
            th.$router.go();
          }, 1000);
        });
    },
    goPost() {
      this.$router.push("/posts");
    },
  },
  created() {
    this.authUser();
  },
  watch: {
    bpage: function (v) {
      this.pageCheck();
    },
  },
};
</script>

<style>
#tt td {
  text-align: center;
  vertical-align: middle;
}
</style>
