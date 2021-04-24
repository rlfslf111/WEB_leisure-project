<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <!-- background image -->
    <div class="info-img" style="display:block;">
      <div class="info-bg"></div>
    </div>
    <div class="user my-5" id="join">
      <div class="wrapC table">
        <div class="middle">
          <h1 class="text-center font-weight-bold">회원가입</h1>
          <div class="form-wrap mt-3 mb-3">
            <!-- 라디오 박스 -->
            <input
              style="cursor: pointer;"
              class="mr-1"
              type="radio"
              id="normal"
              name="type"
              value="normal"
              v-model="checkType"
            />
            <i style="cursor: pointer;" class="fas fa-child mr-1"></i>
            <label style="cursor: pointer;font-size:1.2rem;font-weight:bold;" for="normal">일반 사용자 &emsp;</label>

            <input
              style="cursor: pointer;"
              class="mr-1 ml-5"
              type="radio"
              id="business"
              name="type"
              value="business"
              v-model="checkType"
            />
            <i style="cursor: pointer;" class="fas fa-user-tie mr-1"></i>
            <label style="cursor: pointer;font-size:1.2rem;font-weight:bold;" for="business">사업자</label>
            <br />
            <div
              class="error-text my-4"
              v-if="error.checkType"
              style="color:crimson;font-size:1rem;font-weight:bold;"
            >{{ error.checkType }}</div>

            <Business
              v-if="checkType == 'business'"
              :checkType="checkType"
              @join-create-business="joinBusiness"
            />
            <Normal
              v-if="checkType == 'normal'"
              :checkType="checkType"
              @join-create-normal="joinNormal"
            />
          </div>
        </div>
      </div>
    </div>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
  </div>
</template>

<script>
import PV from "password-validator";
import * as EmailValidator from "email-validator";
import axios from "axios";
import "../../assets/css/join.css";

import Business from "../../components/joinform/Business.vue";
import Normal from "../../components/joinform/Normal.vue";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    Business,
    Normal,
  },
  created() {
    this.checkForm();
  },

  watch: {
    checkType: function (v) {
      this.checkForm();
    },
  },
  methods: {
    checkForm() {
      if (this.checkType == "") {
        this.error.checkType = "사용자 구분을 해주세요.";
      } else {
        this.error.checkType = false;
      }
    },
    joinBusiness(
      email,
      nickname,
      password,
      name,
      checkType,
      file,
      clocation,
      cphone
    ) {
      let data = {
        name,
        nickname,
        email,
        password,
        checkType,
        file,
        clocation,
        cphone,
      };
      axios
        .post(`${baseURL}/account/signup`, data)
        .then((response) => {
          if (response.data == 1) {
            const Toast = Swal.mixin({
              toast: true,
              position: "top-end",
              showConfirmButton: false,
              timer: 2000,
              timerProgressBar: true,
              onOpen: (toast) => {
                toast.addEventListener("mouseenter", Swal.stopTimer);
                toast.addEventListener("mouseleave", Swal.resumeTimer);
              },
            });

            Toast.fire({
              icon: "error",
              title: "사용이 제한된 회원입니다.",
            });
          }
          var formData = new FormData();
          formData.append("file", file);
          axios
            .post(`${baseURL}/account/file/${email}`, formData, {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            })
            .then(function (response) {
            })
            .catch(function (error) {
              console.log(error);
            });
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
            title: "회원 가입을 축하합니다!",
          });
          scroll(0, 0);
          this.$router.push("/");
        })
        .catch((err) => {
          Swal.fire({
            width: 300,
            icon: "error",
            text: "회원 정보를 모두 입력해주세요.",
            confirmButtonColor: "#fff",
            confirmButtonText: '<small style:"font-size:0.8rem;color:black;">확인</small>',
          });
        });
    },
    joinNormal(email, name, nickname, password, checkType, file) {
      let data = {
        name,
        nickname,
        email,
        password,
        checkType,
        file,
      };
      axios
        .post(`${baseURL}/account/signup`, data)
        .then((response) => {
          if (response.data == 1) {
            const Toast = Swal.mixin({
              toast: true,
              position: "top-end",
              showConfirmButton: false,
              timer: 2000,
              timerProgressBar: true,
              onOpen: (toast) => {
                toast.addEventListener("mouseenter", Swal.stopTimer);
                toast.addEventListener("mouseleave", Swal.resumeTimer);
              },
            });

            Toast.fire({
              icon: "error",
              title: "사용이 제한된 회원입니다.",
            });
          }
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
          var formData = new FormData();
          formData.append("file", file);
          axios
            .post(`${baseURL}/account/file/${email}`, formData, {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            })
            .then(function (response) {
            })
            .catch(function (error) {
              console.log(error);
            });
          Toast.fire({
            icon: "success",
            title: "회원 가입을 축하합니다!",
          });
          this.$router.push("/");
        })
        .catch(() => {
          Swal.fire({
            width: 300,
            icon: "error",
            text: "회원 정보를 모두 입력해주세요.",
            confirmButtonColor: "#fff",
            confirmButtonText: '<small style:"font-size:0.8rem;color:black;">확인</small>',
          });
        });
    },
  },
  data: () => {
    return {
      checkType: "",
      // email: "",
      // name: "",
      // nickname: "",
      // password: "",
      // passwordconfirm: "",
      // passwordSchema: new PV(),
      error: {
        // email: false,
        // password: false,
        // nickname: false,
        // passwordconfirm: false,
        checkType: false,
      },
      // isTerm: false,
      // passwordType: "password",
      // passwordConfirmType: "password"
    };
  },
};
</script>
