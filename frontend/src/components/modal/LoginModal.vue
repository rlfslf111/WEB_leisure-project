<template>
  <div
    class="modal fade"
    id="LoginModal"
    tabindex="-1"
    role="dialog"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content" style="width:80%; border:0">
        <div
          class="modal-header pl-0"
          style="background-color:#ccdced; width:100%"
        >
          <h4
            class="modal-title font-weight-bold my-auto ml-2"
            style="width:60%;"
          >
            <i class="fas fa-file-signature ml-5" style="white-space:nowrap">ＬＯＧＩＮ</i>
          </h4>
          <button
            type="button"
            class="close my-auto ml-0"
            data-dismiss="modal"
            aria-label="Close"
          >
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <!-- email 입력칸 -->
        <div class="modal-body d-flex justify-content-center mt-2">
          <i
            v-if="!error.email"
            class="fas fa-envelope my-auto mr-2"
            style="font-size:25px"
          ></i>
          <i
            v-if="error.email"
            class="fas fa-envelope my-auto mr-2"
            style="font-size:25px; color:red"
            title="이메일 형식이 아닙니다!"
          ></i>
          <input
            class="text-center"
            style="width:60%; border-radius:5px;"
            type="text"
            v-model="email"
            placeholder="email@example.com"
          />
        </div>
        <!-- <small class="d-flex justify-content-end mt-0 mb-0" style="width:82%" v-if="error.email"><i class="fas fa-exclamation mr-1 my-auto"></i>{{error.email}}</small> -->

        <!-- password 입력칸 -->
        <div class="modal-body d-flex justify-content-center mt-2">
          <i class="fas fa-lock my-auto mr-2" style="font-size:25px"></i>
          <input
            class="text-center"
            style="width:60%; border-radius:5px;"
            type="password"
            v-model="password"
            placeholder="Input your password"
            @keypress.enter="login"
          />
        </div>

        <div style="margin-left:10%; margin-right:10%">
          <hr />
        </div>

        <!-- 로그인 -->
        <div
          @click="login"
          data-dismiss="modal"
          class="mt-2 row"
          style="cursor: pointer; margin-left:18%; margin-right:18%;"
        >
          <div
            class="col-12 d-flex justify-content-center align-items-center"
            style="border:1px solid RGB(134, 165, 212); height:2rem; border-radius:5px"
          >
            <i class="fas fa-sign-in-alt"
              ></i
            ><span class="my-auto ml-2" style="font-weight:bold">로그인</span>
          </div>
        </div>

        <!-- 회원가입 -->
        <div
          @click="join"
          data-dismiss="modal"
          class="mt-2 row"
          style="cursor: pointer; margin-left:18%; margin-right:18%;"
        >
          <div
            class="col-12 d-flex justify-content-center align-items-center"
            style="border:1px solid RGB(134, 165, 212); height:2rem; border-radius:5px"
          >
            <i class="far fa-user mr-1"
              ></i
              ><span class="my-auto ml-2" style="font-weight:bold"
                >회원가입</span
            >
          </div>
        </div>

        <!-- 비밀번호 찾기 -->
        <div
          data-dismiss="modal"
          class="mt-2 row"
          style="cursor: pointer; margin-left:18%; margin-right:18%;"
          data-toggle="modal"
          data-target="#findpassword"
        >
          <div
            class="col-12 d-flex justify-content-center align-items-center"
            style="border:1px solid RGB(134, 165, 212); height:2rem; border-radius:5px"
          >
            <i class="fas fa-unlock">
            </i>
            <span class="my-auto ml-2" style="font-weight:bold">비밀번호 찾기</span>
          </div>
        </div>

        <!-- 카카오톡 로그인 -->
        <div
          id="kakao-login-btn"
          @click="kakaoLogin"
          class="mt-2 row d-flex justify-content-center"
          style="cursor: pointer; margin-left:18%; margin-right:18%;"
        >
          <div
            class="col-12 d-flex justify-content-center align-items-center"
            style="border:1px solid RGB(134, 165, 212); height:2rem; border-radius:5px"
          >
            <img
              src="../../assets/img/kakaologin2.jpg"
              style="width:20px"
            /><span class="my-auto ml-2" style="font-weight:bold"
              >카카오 로그인</span
            >
          </div>
        </div>

        <div class="mt-2" style="margin-left:10%; margin-right:10%;">
          <hr />
        </div>

        <div class="modal-footer border-0" style=""></div>
      </div>
    </div>
  </div>
</template>

<script>
import PV from "password-validator";
import * as EmailValidator from "email-validator";
import axios from "axios";
import Swal from "sweetalert2";

const baseURL = process.env.VUE_APP_BACKURL;
export default {
  name: "Post",
  components: {},
  created() {
    this.passwordSchema
      .is()
      .min(8)
      .is()
      .max(100)
      .has()
      .digits()
      .has()
      .letters();
  },
  watch: {
    password: function(v) {
      this.checkForm();
    },
    email: function(v) {
      this.checkForm();
    },
  },

  methods: {
    checkForm() {
      if (this.email.length > 0 && !EmailValidator.validate(this.email))
        this.error.email = "이메일 형식이 아닙니다.";
      else if (this.email.length > 0 && EmailValidator.validate(this.email)) {
        axios
          .get(`${baseURL}/account/checkEmail/${this.email}`)
          .then((response) => {
            this.error.email = "";
          })
          .catch(() => {});
      } else this.error.email = false;
    },
    login() {
      if (!this.email && this.password) {
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
          title: "이메일을 적어주세요!",
        });
      } else if (this.email && !this.password) {
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
          title: "비밀번호를 적어주세요!",
        });
      } else if (!this.email && !this.password) {
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
          title: "이메일 및 비밀번호를 적어주세요!",
        });
      } else {
        var tempToken = "";
        axios
          .get(`${baseURL}/account/login/${this.email}/${this.password}`)
          .then((response) => {
            tempToken = response.data;
            axios
              .get(`${baseURL}/report/reports/${this.email}`)
              .then((response) => {
                if (response.data == 0) {
                  this.$cookies.set("Auth-Token", tempToken);
                  const Toast = Swal.mixin({
                    toast: true,
                    position: "top-end",
                    showConfirmButton: true,
                    onOpen: (toast) => {
                      toast.addEventListener("mouseenter", Swal.stopTimer);
                      toast.addEventListener("mouseleave", Swal.resumeTimer);
                    },
                  });
                  Toast.fire({
                    icon: "warning",
                    title: "해당 아이디는 신고 누적으로 차후에 이용이 제한될 수 있습니다.",
                  }).then((result) => {
                    if (result.value) {
                      this.$router.push("/").catch((err) => {
                        console.log(err);
                      });
                      this.$router.go();
                    }
                  });
                } else if (response.data == 1) {
                  const Toast = Swal.mixin({
                    toast: true,
                    position: "top-end",
                    showConfirmButton: true,
                    onOpen: (toast) => {
                      toast.addEventListener("mouseenter", Swal.stopTimer);
                      toast.addEventListener("mouseleave", Swal.resumeTimer);
                    },
                  });
                  Toast.fire({
                    icon: "warning",
                    title:
                      "해당 아이디는 신고 누적으로 이용이 제한되었습니다.",
                  }).then((result) => {
                    if (result.value) {
                      this.$router.push("/").catch((err) => {
                        console.log(err);
                      });
                      this.$router.go();
                    }
                  });
                } else {
                  this.$cookies.set("Auth-Token", tempToken);
                  this.$router.push("/").catch((err) => {
                    console.log(err);
                  });
                  this.$router.go();
                }
              })
              .catch((err) => {
                console.log(err);
              });
          })
          .catch((err) => {
            console.log(err.response.data.status);
            if (err.response.status == 400) {
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
                title: "이메일 또는 비밀번호가 올바르지 않습니다.",
              });
            }
            this.email = "";
            this.password = "";
          });
      }
    },
    join: function() {
      this.$router.push("/user/join/");
      this.$router.go();
    },
    kakaoLogin() {
      let x = this;
      var kakaotempToken = "";
      Kakao.Auth.login({
        // container: "#kakao-login-btn",
        success: function(authObj) {
          Kakao.API.request({
            url: "/v2/user/me",
            success: function(res) {
              x.kakao.email = res.kakao_account.email;
              x.kakao.nickname = res.properties.nickname;

              axios
                .post(`${baseURL}/account/kakaologin`, x.kakao)
                .then((response) => {
                  kakaotempToken = response.data;

                  axios
                    .get(`${baseURL}/report/reports/${x.kakao.email}`)
                    .then((response) => {
                      if (response.data == 0) {
                        const Toast = Swal.mixin({
                          toast: true,
                          position: "top-end",
                          showConfirmButton: true,
                          onOpen: (toast) => {
                            toast.addEventListener("mouseenter", Swal.stopTimer);
                            toast.addEventListener("mouseleave", Swal.resumeTimer);
                          },
                        });
                        Toast.fire({
                          icon: "warning",
                          title: "해당 아이디는 신고 누적으로 차후에 이용이 제한될 수 있습니다.",
                        }).then((result) => {
                          if (result.value) {
                            x.$router.push("/").catch((err) => {
                              console.log(err);
                            });
                            x.$router.go();
                          }
                        });
                        x.$cookies.set("Auth-Token", kakaotempToken);
                      } else if (response.data == 1) {
                        const Toast = Swal.mixin({
                          toast: true,
                          position: "top-end",
                          showConfirmButton: true,
                          onOpen: (toast) => {
                            toast.addEventListener("mouseenter", Swal.stopTimer);
                            toast.addEventListener("mouseleave", Swal.resumeTimer);
                          },
                        });
                        Toast.fire({
                          icon: "warning",
                          title:
                            "해당 아이디는 신고 누적으로 이용이 제한되었습니다.",
                        }).then((result) => {
                          if (result.value) {
                            x.$router.push("/").catch((err) => {
                              console.log(err);
                            });
                            x.$router.go();
                          }
                        });
                      } else {
                        x.$cookies.set("Auth-Token", kakaotempToken);
                        x.$router.go();
                      }
                    })
                    .catch((err) => {
                      console.log(err.response);
                    });

                  // x.$router.push("/");
                  // x.$router.go();
                })
                .catch((err) => {
                  console.log(err);
                });
            },
          });
        },

        fail: function(error) {
          alert(JSON.stringify(error));
        },
      });
    },
  },
  data: () => {
    return {
      email: "",
      password: "",
      passwordSchema: new PV(),
      error: {
        email: false,
        password: false,
      },
      passwordType: "password",
      kakao: {
        email: "",
        nickname: "",
      },
    };
  },
};
</script>

<style>
.modal-open {
  overflow: hidden !important;
}
</style>