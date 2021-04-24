<template>
  <div
    class="modal fade"
    id="indict"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">
            <i class="fas fa-user-tie">
              <span class="ml-2">{{ this.nickname }}</span>
            </i>
            <small>님, 해당 게시글을 신고하시겠습니까?</small>
          </h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <!-- 게시글 정보 -->
          <span class="d-flex mb-3" style="font-weight:bold"> 신고 게시글 정보를 확인해주세요 </span>
          <img
            style="width:100%; height:40%"
            v-if="post.imgurl"
            :src="makeimgurl(post.imgurl)"
            :alt="post.title"
          />
          <img
            style="width:100%; height:40%"
            v-if="!post.imgurl"
            src="../../assets/img/noimage.jpg"
            :alt="post.title"
          />
          <p class="mt-2 d-flex mb-1" style="font-weight:bold">
            상품명 </p>
            <p class="d-flex" style="color:gray">{{ post.title }}</p>
          
          <p class="mt-2 d-flex mb-1" style="font-weight:bold">
            활동명 </p>
            <p class="d-flex" style="color:gray">{{ post.activity }}</p>
          
          <p class="mt-2 d-flex mb-1" style="font-weight:bold">
            업체명 </p>
            <p class="d-flex" style="color:gray">{{ post.companyInfo }}</p>
          
          <hr />
          <div class="form-group">
            <label for="exampleInputEmail1" class="d-flex mt-2">
              <i class="far fa-comment my-auto" ></i>
                <span class="ml-1" style="font-weight:bold">신고 사유를 작성하세요.</span>
              
            </label>
            <textarea
              v-model="indictData.reason"
              type="text"
              class="form-control"
              id="name"
              aria-describedby="emailHelp"
            />
            <small id="emailHelp" class="form-text text-muted d-flex">해당 내용은 관리자에게 전송됩니다.</small>
            <!-- <span class="error-text d-flex mt-1" style="color:crimson;">{{error.name}}</span> -->
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" style="background-color:lightgray; color:white;font-weight:bold;" data-dismiss="modal">Close</button>
          <button
            type="button"
            class="btn btn-default"
            data-dismiss="modal"
            style="background-color:#86a5d4; color:white;font-weight:bold;"
            @click="indictReceipt"
          >신고 접수</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
const baseURL = process.env.VUE_APP_BACKURL;
export default {
  data() {
    return {
      nickname: "",
      indictData: {
        pid: 0,
        email: "",
        remail: "",
        reason: "",
      },
    };
  },
  props: {
    post: [Object, Array],
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.nickname = response.data.nickname;
          this.indictData.email = response.data.email;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    indictReceipt() {
      if (this.indictData.reason.length <= 0) {
        Swal.fire({
          width: 300,
          icon: "error",
          text: "신고 사유를 적어주세요!",
        });
      } else {
        Swal.fire({
          width: 300,
          text: "접수를 진행하시겠습니까?",
          icon: "question",
          showCancelButton: true,
          confirmButtonColor: "#fff",
          cancelButtonColor: "#fff",
          confirmButtonText:
            '<small style="font-weight:bold; font-size:1rem;color:black;">접수</small>',
          cancelButtonText:
            '<small style="font-weight:bold; font-size:1rem;color:black;">취소</small>',
        }).then((result) => {
          if (result.value) {
            axios
              .post(`${baseURL}/report/regist`, this.indictData)
              .then((respone) => {
                this.indictData.reason = "";
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
                  icon: "success",
                  title: "신고 접수가 완료되었습니다!",
                });
                // setTimeout(() => {
                //     this.$router.go()
                // },1000)
              })
              .catch((error) => {
                console.log(error);
              });
          }
        });
      }
    },
  },
  updated() {
    this.indictData.pid = this.post.pid;
    this.indictData.remail = this.post.email;
    if (this.$cookies.get("Auth-Token") != null) {
      this.authUser();
    }
  },
};
</script>

<style></style>
