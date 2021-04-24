<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <!-- background image -->
    <div class="admin-img" style="display:block;">
      <div class="basket-img-bg"></div>
      <div class="postment d-flex ml-5">관리페이지</div>
    <p class="postmentp d-flex ml-5">모든 회원들의 정보를 관리할 수 있습니다</p>
    </div>
    <div class="container col-md-11 mt-5">
      <b-tabs content-class="mt-3" fill justified active-nav-item-class="font-weight-bold text-primary">
        <!-- 포스트 관리 -->
        <b-tab class=" admin-post" title="포스트 관리" active>
          <!-- 포스트관리 웹버전 -->
          <div class="Webtable d-none d-sm-block">
          <table class="table my-5">
            <thead class="thead-dark">
              <tr>
                <th class="admin-post-num">No</th>
                <th class="admin-post-title">제목</th>
                <th class="admin-post-company">업체정보</th>
                <th class="admin-post-email">작성자</th>
                <th class="admin-post-date">요청 날짜</th>
                <th class="admin-post-out">승인 및 거절</th>
              </tr>
            </thead>
            <tbody>
              <tr id="tt" v-for="(post, index) in postlists" :key="index">
                <td style="font-weight:bold;">{{ (ppage - 1) * 10 + index + 1 }}</td>
                <td class="admin-data" style="padding:10px;font-weight:bold;">{{ post.title }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ post.companyInfo }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ post.email }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ permitdate(post.createDate) }}</td>
                <td>
                  <button
                    @click="gopostdetail(post.pid)"
                    type="button"
                    class="btn btn-sm ml-0"
                    style="color:white; background-color:RGB(134, 165, 212); border-radius:7px;font-weight:bold;"
                  >
                    상세보기
                  </button>
                </td>
                <!-- <td><button @click="gopostdetail(post.pid)">상세보기</button></td> -->
                <!-- <td><button @click="postlistData(post)" data-toggle="modal" data-target="#postpermit">상세보기</button></td> -->
                <!-- <td><button @click="postpermit(post.pid)">승인</button> <button @click="postreject(post.pid)">거절</button></td> -->
              </tr>
            </tbody>
          </table>
          <!-- paging -->
          <b-pagination v-model="ppage" v-if="ptotalPage > 10" :total-rows="ptotalPage" pills :per-page="10"></b-pagination>
          <!-- <PostPermitDetailModal :post="postlistDataReceive"/> -->
          </div>

          <!-- 포스트관리 모바일버전 -->
          <div class="MoblieCard d-block d-sm-none d-md-none">
            <table class="table my-5">
            <thead class="thead-dark">
              <tr>
                <th style="width:15%">No</th>
                <th style="width:60%">제목</th>
                <th style="width:25%">관리</th>
              </tr>
            </thead>
            <tbody>
              <tr id="tt" v-for="(post, index) in postlists" :key="index">
                <td>{{ (ppage - 1) * 10 + index + 1 }}</td>
                <td class="admin-data" style="padding:10px">{{ post.title }}</td>
                <td>
                  <button
                    @click="gopostdetail(post.pid)"
                    type="button"
                    class="btn btn-sm ml-0"
                    style="color:white; background-color:RGB(134, 165, 212); border-radius:7px;"
                  >
                    관리
                  </button>
                </td>
                <!-- <td><button @click="gopostdetail(post.pid)">상세보기</button></td> -->
                <!-- <td><button @click="postlistData(post)" data-toggle="modal" data-target="#postpermit">상세보기</button></td> -->
                <!-- <td><button @click="postpermit(post.pid)">승인</button> <button @click="postreject(post.pid)">거절</button></td> -->
              </tr>
            </tbody>
          </table>
          <!-- paging -->
          <b-pagination v-model="ppage" v-if="ptotalPage > 10" :total-rows="ptotalPage" pills :per-page="10"></b-pagination>
          </div>

        </b-tab>

        <!-- 회원 관리 -->
        <b-tab class="admin-info" title="회원관리">
          <!-- 회원관리 웹버전 -->
          <div class="Webtable d-none d-sm-block">
          <table class="table my-5">
            <thead class="thead-dark">
              <tr>
                <th class="admin-info-num">No</th>
                <th class="admin-info-email">이메일</th>
                <th class="admin-info-nickname">닉네임</th>
                <th class="admin-info-name">이름</th>
                <th class="admin-info-type">구분</th>
                <th class="admin-info-out">탈퇴</th>
              </tr>
            </thead>
            <tbody>
              <tr id="tt" v-for="(user, index) in users" :key="index">
                <td style="font-weight:bold;">{{ (upage - 1) * 10 + index + 1 }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ user.email }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ user.nickname }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ user.name }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ user.checkType }}</td>
                <td class="pl-1" v-if="user.uid != 1">
                  <button class="admin-info-button btn btn-sm btn-outline-danger" @click="dropuser(user.uid)">탈퇴처리</button>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- paging -->
          <b-pagination v-model="upage" v-if="utotalPage > 10" :total-rows="utotalPage" pills :per-page="10"></b-pagination>
          </div>

           <!-- 포스트관리 모바일버전 -->
          <div class="MoblieCard d-block d-sm-none d-md-none">
            <table class="table my-5">
            <thead class="thead-dark">
              <tr>
                <th style="width:15%">No</th>
                <th style="width:35%">이메일</th>
                <th style="width:25%">구분</th>
                <th style="width:25%">탈퇴</th>
              </tr>
            </thead>
            <tbody>
              <tr id="tt" v-for="(user, index) in users" :key="index">
                <td>{{ (upage - 1) * 10 + index + 1 }}</td>
                <td class="admin-data">{{ user.email }}</td>
                <td class="admin-data">{{ user.checkType }}</td>
                <td class="pl-1" v-if="user.uid != 1">
                  <button class="admin-info-button btn btn-sm btn-outline-danger" @click="dropuser(user.uid)">탈퇴처리</button>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- paging -->
          <b-pagination v-model="upage" v-if="utotalPage > 10" :total-rows="utotalPage" pills :per-page="10"></b-pagination>
          </div>

        </b-tab>

        <!-- 신고 관리 -->
        <b-tab title="신고 관리">
          <!-- 회원관리 웹버전 -->
          <div class="Webtable d-none d-sm-block">
          <table class="table my-5">
            <thead class="thead-dark">
              <tr>
                <th class="admin-black-num">No</th>
                <th class="admin-black-email">신고한사람</th>
                <th class="admin-black-remail">신고당한사람</th>
                <th class="admin-black-content">내용</th>
                <th class="admin-black-detail">확인</th>
                <th class="admin-black-out">구분</th>
              </tr>
            </thead>
            <tbody>
              <tr id="tt" v-for="(blacklist, index) in blacklists" :key="index">
                <td style="font-weight:bold;">{{ (bpage - 1) * 10 + index + 1 }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ blacklist.email }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ blacklist.remail }}</td>
                <td class="admin-data" style="font-weight:bold;">{{ blacklist.reason }}</td>
                <td class="">
                  <button
                    class="admin-black-view btn-sm"
                    style="color:white; background-color:RGB(134, 165, 212); border-radius:7px;"
                    @click="blacklistData(blacklist)"
                    data-toggle="modal"
                    data-target="#blacklist"
                  >
                    확인
                  </button>
                </td>
                <td class="pl-1">
                  <div class="btn-group btn-group-sm">
                    <button type="button" class="btn btn-danger">처리</button>
                    <button type="button" class="btn btn-danger dropdown-toggle dropdown-toggle-split" data-toggle="dropdown">
                      <span class="caret"></span>
                    </button>
                    <div class="dropdown-menu">
                      <a class="dropdown-item admin-drop-item" style="cursor:pointer" @click="warnblack(blacklist.rpid)">경고</a>
                      <a class="dropdown-item admin-drop-item" style="cursor:pointer" @click="dropblack(blacklist.rpid)">삭제</a>
                      <a class="dropdown-item admin-drop-item" style="cursor:pointer" @click="cancelblack(blacklist.rpid)">취소</a>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <BlackListDetailModal
            :blacklist="blacklistDataReceive"
            @warn-black="warnblack"
            @drop-black="dropblack"
            @cancel-black="cancelblack"
          />
          <!-- paging -->
          <b-pagination v-model="bpage" v-if="btotalPage > 10" :total-rows="btotalPage" pills :per-page="10"></b-pagination>
          </div>

          <!-- 포스트관리 모바일버전 -->
          <div class="MoblieCard d-block d-sm-none d-md-none">
          <table class="table my-5">
            <thead class="thead-dark">
              <tr>
                <th style="width:15%">No</th>
                <th style="width:35%">신고당한사람</th>
                <th style="width:17%">확인</th>
                <th style="width:23%">구분</th>
              </tr>
            </thead>
            <tbody>
              <tr id="tt" v-for="(blacklist, index) in blacklists" :key="index">
                <td>{{ (bpage - 1) * 10 + index + 1 }}</td>
                <td class="admin-data">{{ blacklist.remail }}</td>
                <td class="pl-1">
                  <button
                    class="admin-black-view btn-sm"
                    style="color:white; background-color:RGB(134, 165, 212); border-radius:7px;"
                    @click="blacklistData(blacklist)"
                    data-toggle="modal"
                    data-target="#blacklist"
                  >
                    확인
                  </button>
                </td>
                <td class="pl-1">
                  <div class="btn-group btn-group-sm">
                    <button type="button" class="btn btn-danger" style="font-weight:bold">처리</button>
                    <button type="button" class="btn btn-danger dropdown-toggle dropdown-toggle-split" data-toggle="dropdown">
                      <span class="caret"></span>
                    </button>
                    <div class="dropdown-menu">
                      <a class="dropdown-item admin-drop-item" @click="warnblack(blacklist.rpid)">경고</a>
                      <a class="dropdown-item admin-drop-item" @click="dropblack(blacklist.rpid)">삭제</a>
                      <a class="dropdown-item admin-drop-item" @click="cancelblack(blacklist.rpid)">취소</a>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <BlackListDetailModal
            :blacklist="blacklistDataReceive"
            @warn-black="warnblack"
            @drop-black="dropblack"
            @cancel-black="cancelblack"
          />
          <!-- paging -->
          <b-pagination v-model="bpage" v-if="btotalPage > 10" :total-rows="btotalPage" pills :per-page="10"></b-pagination>
          </div>
        </b-tab>
      </b-tabs>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import '../../assets/css/admin.css';
import BlackListDetailModal from '../../components/modal/BlackListDetailModal.vue';
import BPagenation from 'bootstrap-vue';
// import PostPermitDetailModal from '../../components/modal/PostPermitDetailModal.vue'

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    BlackListDetailModal,
    BPagenation,
    // PostPermitDetailModal,
  },
  data() {
    return {
      utotalPage: 0,
      upage: 1,
      ptotalPage: 0,
      ppage: 1,
      btotalPage: 0,
      bpage: 1,
      postlists: [],
      users: [],
      blacklists: [],
      blacklistDataReceive: {},
      postlistDataReceive: {},
      hashTag: [],
      springCheck: '',
      summerCheck: '',
      autumnCheck: '',
      winterCheck: '',
      placeCheck: '',
    };
  },
  created() {
    this.init();
    this.postlist();
    this.userlist();
    this.blackuser();
  },
  methods: {
    init() {
      // 포스트수 가져오기
      axios
        .get(`${baseURL}/post/countStandpost`)
        .then((res) => {
          this.ptotalPage = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
      // 회원수 가져오기
      axios
        .get(`${baseURL}/account/countAllUser`)
        .then((res) => {
          this.utotalPage = res.data;
        })
        .catch((err) => {
          console.log(err);
        });

      // 블랙리스트수 가져오기
      axios
        .get(`${baseURL}/report/count`)
        .then((res) => {
          this.btotalPage = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    postlist() {
      // 포스트 리스트 가져오기
      axios
        .get(`${baseURL}/post/standpost/${this.ppage}`)
        .then((res) => {
          this.postlists = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    userlist() {
      // 회원정보 가져오기
      axios
        .get(`${baseURL}/account/viewAllUser/${this.upage}`)
        .then((res) => {
          this.users = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    blackuser() {
      // 블랙리스트 가져오기
      axios
        .get(`${baseURL}/report/list/${this.bpage}`)
        .then((res) => {
          this.blacklists = res.data.content;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    cancelblack(rpid) {
      axios
        .delete(`${baseURL}/report/cancel/${rpid}`)
        .then(() => {
          this.blackuser();
        })
        .catch((err) => {
          console.log(err);
        });
    },

    dropuser(uid) {
      Swal.fire({
        width: 350,
        text: '탈퇴시키겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#fff',
        cancelButtonColor: '#fff',
        confirmButtonText: '<a style="font-size:1rem; color:black;">탈퇴</a>',
        cancelButtonText: '<a style="font-size:1rem; color:black;">취소</a>',
      }).then((result) => {
        if (result.value) {
          const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            onOpen: (toast) => {
              toast.addEventListener('mouseenter', Swal.stopTimer);
              toast.addEventListener('mouseleave', Swal.resumeTimer);
            },
          });
          Toast.fire({
            icon: 'success',
            title: '회원 탈퇴가 완료되었습니다.',
          });
          axios
            .delete(`${baseURL}/account/dropUser/${uid}`)
            .then(() => {
              this.userlist();
            })
            .catch((err) => {
              console.log(err);
            });
        }
      });
    },
    dropblack(rpid) {
      axios
        .delete(`${baseURL}/report/dropUser/${rpid}`)
        .then(() => {
          this.blackuser();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    warnblack(rpid) {
      axios
        .get(`${baseURL}/report/warnUser/${rpid}`)
        .then(() => {
          this.blackuser();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    blacklistData(blacklist) {
      this.blacklistDataReceive = blacklist;
    },
    permitdate(createDate) {
      var pd = createDate + '';
      return pd.substring(0, 10) + ' ' + pd.substring(11, 16);
    },
    gopostdetail(pid) {
      scroll(0, 0);
      this.$router.push({
        name: 'AdminPostDetail',
        params: { ID: pid },
      });
    },

    // 승인
    postpermit(pid) {
      axios
        .get(`${baseURL}/post/permit/${pid}`)
        .then((res) => {
          this.postlist();
        })
        .catch((err) => {
          console.log(err);
        });
    },

    // 거절
    postreject(pid) {
      axios
        .delete(`${baseURL}/post/delete/${pid}`)
        .then(() => {
          this.postlist();
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  watch: {
    upage: function(v) {
      this.userlist();
    },
    ppage: function(v) {
      this.postlist();
    },
    bpage: function(v) {
      this.blackuser();
    },
  },
};
</script>

<style>
.admin-drop-item:hover {
  color: red !important;
}
</style>
