<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <!-- background image -->
    <div class="notice-img" style="display:block;">
      <div class="basket-img-bg"></div>
      <div class="postment d-flex ml-5" style="font-weight:bold;">공지사항</div>
    <p class="postmentp d-flex ml-5">링키비티 소식을 알려드려요</p>
    </div>

    <!-- NoticeList -->
    <div class="container col-md-11 mt-5">
      <div class="d-flex justify-content-between notice-main">
        <p class="notice">
          <i class="fas fa-flag mr-2"></i>Notice
        </p>
        <button v-if="this.checkType == 'admin'" class="btn btn-regist" @click="gonoticecreate">
          <i class="fas fa-pen mr-2"></i>공지사항 등록
        </button>
      </div>

      <table class="table">
        <thead class="thead-light">
          <tr>
            <th scope="col" class="table-num">No</th>
            <th scope="col" class="table-title">Title</th>
            <th scope="col" class="table-date">Date</th>
            <th scope="col" class="table-visit">Visit</th>
          </tr>
        </thead>
        <tbody v-if="notices.length > 0">
          <tr v-for="(notice, index) in notices" :key="index">
            <td scope="row">{{ (page - 1) * 10 + (index + 1) }}</td>
            <td class="notice-title" @click="gonoticedetail(notice.nid)">
              <span class="badge badge-pill badge-danger" v-if="notice.importance == 3">필독</span>
              <span class="badge badge-pill badge-warning" v-if="notice.importance == 2">중요</span>
              <span class="badge badge-pill badge-success" v-if="notice.importance == 1">일반</span>
              {{ notice.title }}
            </td>
            <td style="text-overflow:ellipsis;
  overflow: hidden;
  white-space: nowrap;">{{ writeDate(notice.createDate) }}</td>
            <td>{{ notice.visit }}</td>
          </tr>
        </tbody>
      </table>

      <!-- paging -->
      <b-pagination class="pagination" v-model="page" :total-rows="len" pills :per-page="10"></b-pagination>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import "../../assets/css/noticelist.css";
import BPagenation from "bootstrap-vue";
import Swal from "sweetalert2";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  data() {
    return {
      page: 1,
      len: 0,
      notices: {
        nid: "",
        title: "",
        content: "",
        visit: "",
        createDate: "",
      },
      checkType:'',
    };
  },
  created() {
    if (this.$cookies.get("Auth-Token") != null) {
      this.authUser();
    }
    this.init();
  },
  methods: {
    authUser() {
      if(this.$cookies.get("Auth-Token")) {
        axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.checkType = response.data.checkType;
        })
        .catch((err) => {
          console.log(err.response);
        });
      }
    },
    init() {
      
      axios
        .get(`${baseURL}/notice/count`)
        .then((res) => {
          this.len = res.data;
          this.checkPage();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    gonoticecreate() {
      this.$router.push("/noticecreate");
    },
    gonoticedetail(nid) {
      axios
        .put(`${baseURL}/notice/visitPlus/${nid}`)
        .then((res) => {
          this.notices.visit = res.data;
        })
        .catch((err) => {
          console.log(err);
        });

      this.$router.push({
        name: "NoticeListDetail",
        params: { ID: nid },
      });
    },
    writeDate(createDate) {
      var wd = createDate + "";
      return wd.substring(0, 10);
    },
    checkPage() {
      axios
        .get(`${baseURL}/notice/list/${this.page}`)
        .then((res) => {
          this.notices = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
      scroll(0, 0);
    },
  },
  watch: {
    page: function (v) {
      this.checkPage();
    },
  },
};
</script>

<style>
#__BVID__18___BV_tab_button__ {
  height: 50px;
  padding-top: 15px;
  padding-bottom: 0;
}
</style>