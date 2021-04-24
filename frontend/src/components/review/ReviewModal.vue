<template>
  <div
    class="modal fade"
    id="reviewWrite"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">
            <i class="fas fa-user-edit">
              <small class="ml-2" style="font-weight:bold">
                <span style="color:#0047ab">{{this.reviewCreate.nickname}}</span>님, 솔직한 리뷰를 남겨주세요.
              </small>
            </i>
          </h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <!-- img upload -->
          <div class="form-group">
            <label class="d-flex" style="font-weight:bold;">이미지</label>
            <div class align="left">
              <button
                type="button"
                class="btn btn-default btn-sm"
                style="border-radius:35px; font-size:13px; border:1.5px solid"
                @click="onClickImageUpload"
              >사진 업로드</button>
              <img
                class="card-img mb-2"
                v-if="tempimg"
                :src="tempimg"
                style="height: 16rem; width:100%;"
              />
            </div>
            <input ref="file" type="file" hidden @change="onChangeImages" />
            <small v-if="!this.reviewCreate.img" class="form-text text-muted d-flex">원하는 사진을 업로드하세요. (1MB 이하)</small>
            <!-- <small
            v-if="this.reviewCreate.img"
            class="form-text text-muted d-flex"
            >이미지 수정을 원하시면 업로드 버튼을 눌러주세요.</small>-->
          </div>

          <!-- 제목 -->
          <div class="form-group">
            <label for="title" class="d-flex" style="font-weight:bold;">제목</label>
            <input type="text" class="form-control" id="title" v-model="reviewCreate.title" />
            <small id="titlehelp" class="form-text text-muted d-flex" v-if="!error.title">제목을 입력하세요.</small>
            <small
              id="titlehelp"
              class="form-text d-flex"
              v-if="error.title"
              style="color:crimson;"
            >{{error.title}}</small>
          </div>

          <!-- content -->
          <div class="form-group">
            <label for="content" class="d-flex" style="font-weight:bold;">후기</label>
            <textarea
              type="email"
              class="form-control"
              id="content"
              v-model="reviewCreate.content"
              maxlength="90"
            ></textarea>
            <small
              id="titlehelp"
              class="form-text text-muted d-flex"
              v-if="!error.content"
            >후기를 입력하세요.</small>
            <small
              id="titlehelp"
              class="form-text d-flex"
              v-if="error.content"
              style="color:crimson;"
            >{{error.content}}</small>
          </div>

          <!-- rating -->
          <div>
            <label for="content" class="d-flex" style="font-weight:bold;">평점</label>
            <b-form-rating v-model="reviewCreate.star" variant="primary"></b-form-rating>
            <small class="text-muted d-flex" v-if="!error.star">평점을 입력하세요.</small>
            <small class="d-flex" v-if="error.star" style="color:crimson;">{{error.star}}</small>
          </div>
        </div>
        <div class="modal-footer">
          <button
            @click="reviewRegist"
            type="button"
            class="btn"
            style="background-color:#86a5d4; color:white;"
          >
            <i class="fas fa-plus-circle"></i> 등록
          </button>
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
      reviewCreate: {
        pid: 0,
        email: "",
        img: "",
        title: "",
        content: "",
        star: 0,
        proimg: "",
        nickname: "",
      },
      error: {
        title: false,
        content: false,
        star: false,
      },
      tempimg:"",
    };
  },
  props: {
    pid: [Number, String],
    email: String,
  },
  created() {
    if (this.$cookies.get("Auth-Token") != null) {
      this.authUser();
    }
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.reviewCreate.email = response.data.email;
          this.reviewCreate.pid = this.pid;
          this.reviewCreate.nickname = response.data.nickname;
          this.getProfileImg();
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
     makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    reviewRegist() {
      let check = 0;
      if (this.reviewCreate.title == "") {
        this.error.title = "제목은 빈칸일 수 없습니다.";
        check = 1;
      } else this.error.title = false;
      if (this.reviewCreate.content == "") {
        this.error.content = "후기는 빈칸일 수 없습니다.";
        check = 1;
      } else this.error.content = false;
      if (this.reviewCreate.star == 0) {
        this.error.star = "평점은 1점 이상이어야 합니다.";
        check = 1;
      } else this.error.star = false;
      if (check == 1) {
        return;
      }
      axios
        .post(`${baseURL}/review/regist`, this.reviewCreate)
        .then((response) => {
          this.fileUpload(response.data.rvid);
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
          setTimeout(() => {
            this.$router.go();
          }, 1000);
          Toast.fire({
            icon: "success",
            title: "후기가 등록되었습니다.",
          });
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getProfileImg() {
      axios
        .get(`${baseURL}/account/getImg/${this.reviewCreate.email}`)
        .then((response) => {
          this.reviewCreate.proimg = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    fileUpload(rvid) {
    var formData = new FormData();
    const file = this.$refs.file.files[0];
    if(file != null) {
      formData.append("file", file);
      axios.post(`${baseURL}/review/file/${rvid}`
          ,formData
          , {
            headers: {
              'Content-Type': 'multipart/form-data'
              }
          }
          )
        .then(function (response) {
          })
        .catch(function (error) {
          console.log(error);
        });
      }
    },
    onClickImageUpload() {
      this.$refs.file.click();
    },
    onChangeImages(e) {
      const file = e.target.files[0];
      if(file == null) {
        return;
      }
      if(file.size >= 1048576) {
        const Toast = Swal.mixin({
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 2000,
          timerProgressBar: true,
          onOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer)
            toast.addEventListener('mouseleave', Swal.resumeTimer)
          }
        })

        Toast.fire({
          icon: 'error',
          title: '파일 업로드 크기를 초과하였습니다!'
        })
        return;
      }
      this.tempimg = URL.createObjectURL(file);
    },
  },
};
</script>

<style>
</style>