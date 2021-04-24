<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <!-- background image -->
    <div class="postcreate-img" style="display:block;">
      <div class="basket-img-bg"></div>
      <div class="postment d-flex ml-5" style="font-weight:bold;">액티비티 등록</div>
    <p class="postmentp d-flex ml-5">다양한 액티비티를 등록해보세요</p>
    </div>

    <div class="container col-md-11 mt-3">
      <div class="column">
        <div class="card mt-5 mb-3" style="max-width: 100%">
          <div class="row no-gutters">
            <!-- 이미지 삽입 -->
            <div class="col-md-5">
              <div class="col-md-10 p-0 mr-0" align="left">
                <button
                  type="button"
                  class="btn btn-default btn-sm d-flex mb-1"
                  style="border-radius:10px; font-size:15px; border:1.5px solid; height:2rem"
                  @click="onClickImageUpload"
                >
                  <i class="fas fa-image my-auto">
                    <span class="ml-1">이미지 업로드</span>
                  </i>
                </button>
                <div
                  class="mb-1"
                  style="height: 16rem; width:100%; border-radius:5px; border:1px solid lightgray;"
                >
                  <img
                    class="card-img mb-2"
                    v-if="this.PostCreate.imgurl && !tempcheck"
                    :src="makeimgurl(this.PostCreate.imgurl)"
                    style="height: 16rem; width:100%"
                  />
                  <img
                    class="card-img mb-2"
                    v-if="tempimg && tempcheck"
                    :src="tempimg"
                    style="height: 16rem; width:100%"
                  />
                </div>
              </div>
              <input ref="file" type="file" hidden @change="onChangeImages" />
              <small
                v-if="!this.PostCreate.imgurl"
                class="form-text text-muted d-flex"
              >사진을 업로드하세요. (1MB 이하)</small>
              <!-- <small
                v-if="this.PostCreate.imgurl"
                class="form-text text-muted d-flex"
              >이미지 수정을 원하시면 업로드 버튼을 눌러주세요.</small-->
            </div>

            <div class="col-md-7">
              <div class="card-body" style="padding: 0 0 0 0px">
                <div class="text">
                  <!-- 제목 -->
                  <div class="form-group">
                    <label class="d-flex" style="font-weight:bold;">Title</label>
                    <input type="text" class="form-control" id="title" v-model="PostCreate.title" />
                    <small class="form-text text-muted d-flex" v-if="!error.title">상품명을 입력하세요.</small>
                    <small
                      class="form-text d-flex"
                      style="color:red;"
                      v-if="error.title"
                    >{{ error.title }}</small>
                    <div class="d-flex justify-content-between mt-2 mb-2">
                      <div style="width:49%;">
                        <label class="d-flex justify-content-start" style="font-weight:bold;">Activity</label>
                        <input
                          type="text"
                          class="form-control"
                          id="activity"
                          v-model="PostCreate.activity"
                        />
                        <small
                          class="form-text text-muted d-flex"
                          v-if="!error.activity"
                        >활동명을 입력하세요.</small>
                        <small
                          class="form-text d-flex"
                          style="color:red;"
                          v-if="error.activity"
                        >{{ error.activity }}</small>
                      </div>
                      <!-- 이용 가격 -->
                      <!-- <div class="form-group mb-0"> -->
                      <div style="width:49%;">
                        <label class="d-flex justify-content-start" style="font-weight:bold;">Price</label>
                        <input
                          type="text"
                          class="form-control"
                          id="price"
                          v-model="PostCreate.price"
                        />
                        <small
                          class="form-text text-muted d-flex"
                          v-if="!error.price && !error.priceint"
                        >가격을 입력하세요.</small>
                        <small
                          class="form-text d-flex"
                          style="color:red;"
                          v-if="error.price"
                        >{{ error.price }}</small>
                        <small
                          class="form-text d-flex"
                          style="color:red;"
                          v-if="error.priceint"
                        >{{ error.priceint }}</small>
                        <!-- </div> -->
                      </div>
                    </div>
                  <!-- </div> -->
                  <!-- 사용 기간 -->
                  <!-- <div class="form-group"> -->
                  <label class="d-flex justify-content-start mb-0 mt-0" style="font-weight:bold;">Expiration-Date</label>
                  <div class="d-flex justify-content-between">
                    <small class="form-text text-muted" style="margin-right:auto;font-weight:bold;">시작일</small>
                    <br />
                    <small class="form-text text-muted" style="margin-right:auto;font-weight:bold;">마감일</small>
                  </div>
                  <div class="d-flex justify-content-between">
                    <b-form-datepicker
                      id="sdate"
                      v-model="PostCreate.sdate"
                      :min="min"
                      class="col-md-6 mr-1"
                    ></b-form-datepicker>

                    <b-form-datepicker
                      id="edate"
                      v-model="PostCreate.edate"
                      :min="PostCreate.sdate"
                      class="col-md-6"
                    ></b-form-datepicker>
                  </div>
                  <small class="form-text text-muted d-flex" v-if="!error.sedate">상품 유효기간을 지정해주세요.</small>
                  <small
                    class="form-text d-flex"
                    style="color:red;"
                    v-if="error.sedate"
                  >{{error.sedate}}</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <hr class="mt-0" />
      <div class="row">
        <!-- field 선택 -->
        <div class="form-group col-sm-12 col-md-5" style="width:23rem;">
          <label class="d-flex justify-content-start" style="font-weight:bold;">Field</label>
          <select class="form-control" id="place" v-model="PostCreate.place">
            <option value="ground">Ground</option>
            <option value="water">Water</option>
            <option value="sky">Sky</option>
          </select>
          <small class="form-text text-muted d-flex" v-if="!error.place">필드를 선택하세요.</small>
          <small class="form-text d-flex" style="color:red;" v-if="error.place">{{ error.place }}</small>
        </div>
        <!-- Season 선택 -->
        <div class="form-group col-sm-12 col-md-7">
          <label class="d-flex justify-content-start" style="font-weight:bold;">Seasons</label>
          <div class="d-flex justify-content-between">
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="checkbox"
                id="spring"
                value="spring"
                v-model="seasons"
              />
              <label class="form-check-label" for="spring" >Spring</label>
            </div>
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="checkbox"
                id="summer"
                value="summer"
                v-model="seasons"
              />
              <label class="form-check-label" for="summer">Summer</label>
            </div>
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="checkbox"
                id="autumn"
                value="autumn"
                v-model="seasons"
              />
              <label class="form-check-label" for="autumn">Autumn</label>
            </div>
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="checkbox"
                id="winter"
                value="winter"
                v-model="seasons"
              />
              <label class="form-check-label" for="winter">Winter</label>
            </div>
          </div>
          <small class="form-text text-muted d-flex" v-if="!error.seasons">상품 이용 계절을 선택하세요.(중복가능)</small>
          <small
            class="form-text d-flex"
            style="color:red;"
            v-if="error.seasons"
          >{{ error.seasons }}</small>
        </div>
      </div>
      <div>
        <!-- 업체 정보 -->
        <!-- <h4 class="d-flex mb-2" style="font-weight:bold">업체정보</h4> -->
        <div class="form-group">
          <label class="d-flex justify-content-start" style="font-weight:bold;">Corporation-Detail</label>
          <textarea class="form-control" id="company-information" v-model="PostCreate.companyInfo"></textarea>
          <small class="form-text text-muted d-flex" v-if="!error.companyInfo">업체 정보를 입력하세요.</small>
          <small
            class="form-text d-flex"
            style="color:red;"
            v-if="error.companyInfo"
          >{{ error.companyInfo }}</small>
        </div>

        <hr />

        <!-- 상세 정보 -->
        <!-- <h4 class="d-flex mb-2" style="font-weight:bold">상세정보</h4> -->
        <div class="form-group">
          <label class="d-flex justify-content-start" style="font-weight:bold;">Detail-Info</label>
          <Editor ref="toastuiEditor" />
          <small class="form-text text-muted d-flex" v-if="!error.detail">상품 상세정보를 입력하세요.</small>
          <small class="form-text d-flex" style="color:red;" v-if="error.detail">{{ error.detail }}</small>
        </div>
        <hr />
        <!-- 지도 -->
        <div>
          <label class="d-flex justify-content-start" style="font-weight:bold;">Address</label>
          <div class="d-flex mb-1">
            <button
              type="button"
              class="btn btn-default btn-sm ml-1"
              style="border-radius:10px; font-size:13px; border:1.5px solid"
              @click="Search"
            >
              <span style="font-weight:bold">우편번호 찾기</span>
            </button>
          </div>
          <input
            type="text"
            class="form-control mb-1"
            v-model="addr2"
            placeholder="주소"
            readonly
          />
          <input type="text" class="form-control mb-1" v-model="addr3" placeholder="상세주소" />
        </div>

        <small class="form-text text-muted d-flex">주소를 입력하세요.</small>

        <hr />

        <!-- HasTag -->
        <label for="tags-basic" class="d-flex mt-3" style="font-weight:bold;">#HASHTAG</label>
        <b-form-tags
          input-id="tags-pills"
          v-model="tagValue"
          tag-variant="primary"
          tag-pills
          size="md"
          separator=" "
          placeholder="원하는 태그를 입력해주세요."
          class="mb-2"
        ></b-form-tags>
        <small class="form-text text-muted d-flex" v-if="!error.tagValue">해시태그를 입력해주세요.</small>
        <small class="form-text d-flex" style="color:red;" v-if="error.tagValue">{{error.tagValue}}</small>
      </div>

      <hr class="mt-2" />
      <div class="d-flex justify-content-end mb-5">
        <button
          type="submit"
          class="btn btn-outline mr-1"
          style="font-size: 1rem; color: gray;font-weight:bold;"
          @click="tempSave"
        >임시저장</button>
        <button
          type="submit"
          class="btn btn-defalut"
          style="background-color:#86a5d4; color:white; font-weight:bold;"
          @click="regist"
        >
          <i class="fas fa-pen mr-1"></i>등록하기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
const baseURL = process.env.VUE_APP_BACKURL;

import axios from "axios";
import "../../assets/css/postcreate.css";
import Swal from "sweetalert2";
import "codemirror/lib/codemirror.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/vue-editor";

export default {
  components: {
    Editor,
  },
  data() {
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const minDate = new Date(today);

    return {
      //
      PostCreate: {
        email: "",
        title: "",
        location: "",
        locationdetail:"",
        imgurl: "",
        price: "",
        sdate: "",
        edate: "",
        companyInfo: "",
        detail: "",
        activity: "",
        spring: 0,
        summer: 0,
        autumn: 0,
        winter: 0,
        place: "",
      },
      error: {
        activity: false,
        detail: false,
        price: false,
        companyInfo: false,
        title: false,
        priceint: false,
        location: false,
        seasons: false,
        place: false,
        sedate: false,
        tagValue: false,
      },
      addr1: "",
      addr2: "",
      addr3: "",
      seasons: [],
      tagValue: [],
      tempimg: "",
      tempcheck: false,
      min: minDate,
    };
  },
  watch: {
    PostCreate: {
      handler: function (val) {
        if (!/^[0-9]+$/g.test(val.price) && val.price.length > 0) {
          this.error.priceint = "가격은 숫자만 입력 가능합니다.";
        } else {
          this.error.priceint = false;
        }

        if (val.sdate != "" && val.edate != "" && val.sdate > val.edate) {
          const now = new Date();
          const today = new Date(
            now.getFullYear(),
            now.getMonth(),
            now.getDate()
          );

          val.edate = this.$refs.edate;
        }
      },
      deep: true,
    },
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.PostCreate.email = response.data.email;
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    Search() {
      let x = this;
      new daum.Postcode({
        oncomplete: function (data) {
          x.addr1 = data.zonecode;
          x.addr2 = data.address;
          x.addr3 = data.buildingName;
          x.PostCreate.location = x.addr2;
        },
      }).open();
    },
    regist: function () {
      this.PostCreate.locationdetail = this.addr3;
      var content = this.$refs.toastuiEditor.invoke("getMarkdown");
      this.PostCreate.detail = content;
      var flag = 0;
      if (this.PostCreate.activity == "") {
        this.error.activity = "활동명은 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.activity = false;
      }
      if (this.PostCreate.detail == "") {
        this.error.detail = "상품 세부정보는 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.detail = false;
      }
      if (this.PostCreate.companyInfo == "") {
        this.error.companyInfo = "업체 정보는 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.companyInfo = false;
      }
      if (this.PostCreate.sdate == "" || this.PostCreate.edate == "") {
        this.error.date = "날짜가 빈칸일 수 없습니다";
        flag = 1;
      } else {
        this.error.date = false;
      }
      if (this.PostCreate.price == "") {
        this.error.price = "가격은 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.price = false;
      }
      if (this.PostCreate.title == "") {
        this.error.title = "상품명은 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.title = false;
      }
      if (this.seasons.length == 0) {
        this.error.seasons = "계절은 하나 이상 선택해야합니다.";
        flag = 1;
      } else {
        this.error.seasons = false;
      }
      if (this.tagValue.length == 0) {
        this.error.tagValue = "해시태그는 하나 이상 입력해야합니다.";
        flag = 1;
      } else {
        this.error.tagValue = false;
      }
      if (this.PostCreate.place == "") {
        this.error.place = "필드는 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.place = false;
      }
      if (this.PostCreate.sdate == "" || this.PostCreate.edate == "") {
        this.error.sedate = "유효기간은 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.sedate = false;
      }
      if (flag == 1) {
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
          title: '정보를 모두 입력해주세요!'
        })
        return;
      }
      this.PostCreate.location = this.addr2;

      for (var i = 0; i < this.seasons.length; i++) {
        if (this.seasons[i] == "spring") {
          this.PostCreate.spring = 1;
        } else if (this.seasons[i] == "summer") {
          this.PostCreate.summer = 1;
        } else if (this.seasons[i] == "autumn") {
          this.PostCreate.autumn = 1;
        } else if (this.seasons[i] == "winter") {
          this.PostCreate.winter = 1;
        }
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

      axios
        .post(`${baseURL}/post/regist/${this.tagValue}`, this.PostCreate)
        .then((response) => {
          Toast.fire({
            icon: "success",
            title: "게시물 승인 요청이 완료되었습니다.",
          });
          this.fileUpload(response.data.pid);
          this.$router.push("/posts");
        })
        .catch((error) => {
          console.log(error);
        });
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    tempSave() {
      //임시저장 메소드
      this.PostCreate.locationdetail = this.addr3;
      if (this.PostCreate.title == "") {
        this.error.title = "상품명은 빈칸일 수 없습니다.";
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
          title: '정보를 확인해주세요!'
        })
        return;
      }
      for (var i = 0; i < this.seasons.length; i++) {
        if (this.seasons[i] == "spring") {
          this.PostCreate.spring = 1;
        } else if (this.seasons[i] == "summer") {
          this.PostCreate.summer = 1;
        } else if (this.seasons[i] == "autumn") {
          this.PostCreate.autumn = 1;
        } else if (this.seasons[i] == "winter") {
          this.PostCreate.winter = 1;
        }
      }
      if (this.tagValue == "") {
        axios
          .post(`${baseURL}/temp/regist/nononotag`, this.PostCreate)
          .then((response) => {
            this.fileUpload(response.data.pid);
            const Toast = Swal.mixin({
              toast: true,
              position: "top-end",
              showConfirmButton: false,
              timer: 1000,
              timerProgressBar: true,
              // onOpen: (toast) => {
              //   toast.addEventListener('mouseenter', Swal.stopTimer)
              //   toast.addEventListener('mouseleave', Swal.resumeTimer)
              // }
            });

            Toast.fire({
              icon: "success",
              title: "임시저장 완료!",
            });
            this.$router.push("/posts");
          })
          .catch((error) => {
            console.log(error);
          });
      } else {
        axios
          .post(`${baseURL}/temp/regist/${this.tagValue}`, this.PostCreate)
          .then((response) => {
            this.fileUpload(response.data.pid);
            const Toast = Swal.mixin({
              toast: true,
              position: "top-end",
              showConfirmButton: false,
              timer: 1000,
              timerProgressBar: true,
              // onOpen: (toast) => {
              //   toast.addEventListener('mouseenter', Swal.stopTimer)
              //   toast.addEventListener('mouseleave', Swal.resumeTimer)
              // }
            });

            Toast.fire({
              icon: "success",
              title: "임시저장 완료!",
            });
            this.$router.push("/posts");
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    fileUpload(pid) {
      var formData = new FormData();
      const file = this.$refs.file.files[0];
      if(file != null) {
        formData.append("file", file);
        axios
          .post(`${baseURL}/post/file/${pid}`, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })
          .then(function () {
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
      this.tempcheck = true;
    },
  },
  created() {
    this.authUser();
  },
};
</script>
<style></style>