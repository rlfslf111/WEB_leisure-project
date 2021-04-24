<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <!-- background image -->
    <div class="post-img" style="display:block;"></div>

    <div class="container col-md-11" style="margin-top: 100px">
      <div class="column">
        <div class="card mt-3 mb-3" style="max-width: 100%;">
          <div class="row no-gutters">
            <!-- 이미지 삽입 -->
            <div class="col-md-5">
              <div class="col-md-10 p-0 mr-0" align="left">
                <button
                  type="button"
                  class="btn btn-default btn-sm d-flex mb-1"
                  @click="onClickImageUpload"
                  style="border-radius:10px; font-size:15px; border:1.5px solid;height:2rem;"
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
                    v-if="PostUpdate.imgurl && !tempcheck"
                    :src="makeimgurl(PostUpdate.imgurl)"
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
                v-if="!this.PostUpdate.imgurl"
                class="form-text text-muted d-flex"
                >사진을 업로드하세요. (1MB 이하)</small
              >
              <!-- <small
                v-if="this.PostUpdate.imgurl"
                class="form-text text-muted d-flex"
              >이미지 수정을 원하시면 업로드 버튼을 눌러주세요.</small>-->
            </div>

            <div class="col-md-7">
              <div class="card-body" style="padding: 0 0 0 ">
                <div class="text">
                  <!-- 제목 -->
                  <div class="form-group">
                    <label class="d-flex" style="font-weight:bold;"
                      >Title</label
                    >
                    <input
                      type="text"
                      class="form-control"
                      id="title"
                      v-model="PostUpdate.title"
                    />
                    <small
                      class="form-text text-muted d-flex"
                      v-if="!error.title"
                      >상품명을 입력하세요.</small
                    >
                    <small
                      class="form-text d-flex"
                      style="color:red;"
                      v-if="error.title"
                      >{{ error.title }}</small
                    >
                    <div class="d-flex justify-content-between mt-2 mb-2">
                      <div style="width:49%;">
                        <label
                          class="d-flex justify-content-start"
                          style="font-weight:bold;"
                          >Activity</label
                        >
                        <input
                          type="text"
                          class="form-control"
                          id="activity"
                          v-model="PostUpdate.activity"
                        />
                        <small
                          class="form-text text-muted d-flex"
                          v-if="!error.activity"
                          >활동명을 입력하세요.</small
                        >
                        <small
                          class="form-text d-flex"
                          style="color:red;"
                          v-if="error.activity"
                          >{{ error.activity }}</small
                        >
                      </div>
                      <!-- 이용 가격 -->
                      <div style="width:49%;">
                        <label
                          class="d-flex justify-content-start"
                          style="font-weight:bold;"
                          >Price</label
                        >
                        <input
                          type="text"
                          class="form-control"
                          id="price"
                          v-model="PostUpdate.price"
                        />
                        <small
                          class="form-text text-muted d-flex"
                          v-if="!error.price && !error.priceint"
                          >가격을 입력하세요.</small
                        >
                        <small
                          class="form-text d-flex"
                          style="color:red;"
                          v-if="error.price"
                          >{{ error.price }}</small
                        >
                        <small
                          class="form-text d-flex"
                          style="color:red;"
                          v-if="error.priceint"
                          >{{ error.priceint }}</small
                        >
                      </div>
                    </div>

                    <!-- 사용 기간 -->
                    <label
                      class="d-flex justify-content-start mb-0"
                      style="font-weight:bold;"
                      >Expiration-Date</label
                    >
                    <div class="d-flex justify-content-between">
                      <small
                        class="form-text text-muted"
                        style="margin-right:auto; font-weight:bold;"
                        >시작일</small
                      >
                      <br />
                      <small
                        class="form-text text-muted"
                        style="margin-right:auto; font-weight:bold;"
                        >마감일</small
                      >
                    </div>
                    <div class="d-flex justify-content-between">
                      <b-form-datepicker
                        id="sdate"
                        v-model="PostUpdate.sdate"
                        :min="min"
                        class="col-md-6 mr-1"
                      ></b-form-datepicker>
                      {{ PostUpdate.edate }}
                      <b-form-datepicker
                        id="edate"
                        v-model="PostUpdate.edate"
                        :min="PostUpdate.sdate"
                        class="col-md-6"
                      ></b-form-datepicker>
                    </div>
                    <small
                      class="form-text text-muted d-flex"
                      v-if="!error.sedate"
                      >상품 유효기간을 지정하세요.</small
                    >
                    <small
                      class="form-text d-flex"
                      style="color:red;"
                      v-if="error.sedate"
                      >{{ error.sedate }}</small
                    >
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <hr class="mt-0" />
      <!-- season, place check badge -->
      <div class="row">
        <div class="form-group col-sm-12 col-md-5" style="width:23rem; ">
          <label class="d-flex justify-content-start" style="font-weight:bold;"
            >Field</label
          >
          <select class="form-control" id="place" v-model="PostUpdate.place">
            <option value="ground">Ground</option>
            <option value="water">Water</option>
            <option value="sky">Sky</option>
          </select>
          <small class="form-text text-muted d-flex" v-if="!error.place"
            >필드를 선택하세요.</small
          >
          <small
            class="form-text d-flex"
            style="color:red;"
            v-if="error.place"
            >{{ error.place }}</small
          >
        </div>
        <div class="form-group col-sm-12 col-md-7">
          <label class="d-flex justify-content-start" style="font-weight:bold;"
            >Seasons</label
          >
          <div class="d-flex justify-content-between">
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="checkbox"
                id="spring"
                value="spring"
                v-model="seasons"
              />
              <label class="form-check-label" for="spring">Spring</label>
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
          <small class="form-text text-muted d-flex" v-if="!error.seasons"
            >상품 이용 계절을 선택하세요.(중복가능)</small
          >
          <small
            class="form-text d-flex"
            style="color:red;"
            v-if="error.seasons"
            >{{ error.seasons }}</small
          >
        </div>
      </div>
      <div>
        <!-- 업체 정보 -->
        <!-- <h4 class="d-flex mb-2" style="font-weight:bold">업체정보</h4> -->
        <div class="form-group">
          <label class="d-flex justify-content-start" style="font-weight:bold;"
            >Corporation-Detail</label
          >
          <textarea
            class="form-control"
            id="company-information"
            v-model="PostUpdate.companyInfo"
          ></textarea>
          <small class="form-text text-muted d-flex" v-if="!error.companyInfo"
            >업체 정보를 입력하세요.</small
          >
          <small
            class="form-text d-flex"
            style="color:red;"
            v-if="error.companyInfo"
            >{{ error.companyInfo }}</small
          >
        </div>

        <hr />

        <!-- 상세 정보 -->
        <!-- <h4 class="d-flex mb-2" style="font-weight:bold">상세정보</h4> -->
        <div class="form-group">
          <label class="d-flex justify-content-start" style="font-weight:bold;"
            >Detail-Info</label
          >
          <Editor
            ref="toastuiEditor"
            v-if="PostUpdate.detail != null"
            :initialValue="PostUpdate.detail"
          />
          <small class="form-text text-muted d-flex" v-if="!error.detail"
            >상품 상세정보를 입력하세요.</small
          >
          <small
            class="form-text d-flex"
            style="color:red;"
            v-if="error.detail"
            >{{ error.detail }}</small
          >
        </div>
        <hr />

        <div>
          <label class="d-flex justify-content-start" style="font-weight:bold;"
            >Address</label
          >
          <div class="d-flex mb-1">
            <!-- <input
              type="text"
              class="form-control"
              v-model="addr1"
              style="width:200px;"
              placeholder="우편번호"
            />-->
            <button
              type="button"
              class="btn btn-default btn-sm ml-1"
              style="border-radius:10px; font-size:13px; border:1.5px solid"
              @click="Search"
            >
              우편번호 찾기
            </button>
          </div>
          <input
            type="text"
            class="form-control mb-1"
            v-model="PostUpdate.location"
            placeholder="주소"
            readonly
          />
          <input
            type="text"
            class="form-control mb-1"
            v-model="PostUpdate.locationdetail"
            placeholder="상세주소"
          />
          <!-- <input
            type="text"
            class="form-control mb-1"
            v-model="PostUpdate.location"
            placeholder="상세주소"
          /> -->
        </div>

        <small class="form-text text-muted d-flex">주소를 입력하세요.</small>

        <hr />

        <!-- HasTag -->
        <label for="tags-basic" class="d-flex mt-3" style="font-weight:bold;"
          ># HASHTAG</label
        >
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
        <small class="form-text text-muted d-flex"
          >해시태그를 입력해주세요.</small
        >
      </div>

      <hr class="mt-2" />
      <div class="d-flex justify-content-end mb-5">
        <button
          type="submit"
          class="btn btn-defalut"
          style="background-color:#86a5d4; color:white; font-weight:bold;"
          @click="modify"
        >
          <i class="far fa-edit mr-2"></i>수정하기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Swal from "sweetalert2";
import "codemirror/lib/codemirror.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/vue-editor";

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    Editor,
  },
  data() {
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const minDate = new Date(today);

    return {
      PostUpdate: [],
      pid: "",
      seasons: [],
      tagValue: [],
      addr1: "",
      addr2: "",
      addr3: "",
      tempimg: "",
      tempcheck: false,
      // Instance_Date: []
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
      min: minDate,
    };
  },
  watch: {
    PostUpdate: {
      handler: function(val) {
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
    Search() {
      let x = this;
      new daum.Postcode({
        oncomplete: function(data) {
          x.addr1 = data.zonecode;
          x.addr2 = data.address;

          x.addr3 = data.buildingName;
          x.PostUpdate.location = x.addr2;
          x.PostUpdate.locationdetail = x.addr3;
        },
      }).open();
    },
    fetchDetail: function() {
      axios
        .get(`${baseURL}/post/detail/${this.pid}`)
        .then((response) => {
          this.PostUpdate = response.data;
          console.log(this.PostUpdate);
          this.checkSeason();
        })
        .catch((error) => {
          console.log(error.response.data);
        });
    },
    checkSeason() {
      if (this.PostUpdate.spring == 1) {
        this.seasons.push("spring");
      }
      if (this.PostUpdate.summer == 1) {
        this.seasons.push("summer");
      }
      if (this.PostUpdate.autumn == 1) {
        this.seasons.push("autumn");
      }
      if (this.PostUpdate.winter == 1) {
        this.seasons.push("winter");
      }
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/" + imgurl;
      return url;
    },
    taglist() {
      axios
        .get(`${baseURL}/tag/list/${this.pid}`)
        .then((res) => {
          this.tagValue = res.data;
          // console.log(this.hashTag);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    modify: function() {
      var content = this.$refs.toastuiEditor.invoke("getMarkdown");
      this.PostUpdate.detail = content;
      var flag = 0;
      if (this.PostUpdate.activity == "") {
        this.error.activity = "활동명은 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.activity = false;
      }
      if (this.PostUpdate.detail == "") {
        this.error.detail = "상품 세부정보는 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.detail = false;
      }
      if (this.PostUpdate.companyInfo == "") {
        this.error.companyInfo = "업체 정보는 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.companyInfo = false;
      }
      if (this.PostUpdate.price == "") {
        this.error.price = "가격은 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.price = false;
      }
      if (this.PostUpdate.title == "") {
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
      if (this.PostUpdate.place == "") {
        this.error.place = "필드는 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.place = false;
      }
      if (this.PostUpdate.sdate == "" || this.PostUpdate.edate == "") {
        this.error.sedate = "유효기간은 빈칸일 수 없습니다.";
        flag = 1;
      } else {
        this.error.sedate = false;
      }
      if (this.PostUpdate.location == "") {
        this.error.location = "주소가 빈칸일 수 없습니다.";
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

      this.PostUpdate.spring = 0;
      this.PostUpdate.summer = 0;
      this.PostUpdate.autumn = 0;
      this.PostUpdate.winter = 0;
      for (var i = 0; i < this.seasons.length; i++) {
        if (this.seasons[i] == "spring") {
          this.PostUpdate.spring = 1;
        } else if (this.seasons[i] == "summer") {
          this.PostUpdate.summer = 1;
        } else if (this.seasons[i] == "autumn") {
          this.PostUpdate.autumn = 1;
        } else if (this.seasons[i] == "winter") {
          this.PostUpdate.winter = 1;
        }
      }

      console.log(this.seasons);
      console.log(this.PostUpdate);

      Swal.fire({
        width: 350,
        text: "수정하시겠습니까?",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#fff",
        cancelButtonColor: "#fff",
        confirmButtonText: '<a style="font-size:1rem; color:black">Update</a>',
        cancelButtonText: '<a style="font-size:1rem; color:black">Cancel</a>',
      }).then((result) => {
        if (result.value) {
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
            title: "게시글 수정완료!",
          });
          axios
            .put(`${baseURL}/post/modify/${this.tagValue}`, this.PostUpdate)
            .then((response) => {
              this.fileUpload(response.data.pid);
              this.$router.push({
                name: "PostListDetail",
                params: { ID: this.pid },
              });
            })
            .catch((error) => {
              console.log(error.response.data);
            });
        }
      });
    },

    fileUpload(pid) {
      var formData = new FormData();
      const file = this.$refs.file.files[0];
      if (file != null) {
        formData.append("file", file);
        axios
          .post(`${baseURL}/post/file/${pid}`, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })
          .then(function(response) {})
          .catch(function(error) {
            console.log(error);
          });
      }
    },
    onClickImageUpload() {
      this.$refs.file.click();
    },
    onChangeImages(e) {
      const file = e.target.files[0];
      if (file == null) {
        return;
      }
      if (file.size >= 1048576) {
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
          title: "파일 업로드 크기를 초과하였습니다!",
        });
        return;
      }
      this.tempimg = URL.createObjectURL(file);
      this.tempcheck = true;
    },
  },
  created() {
    this.pid = this.$route.params.ID;
    this.fetchDetail();
    this.taglist();
  },
};
</script>

<style></style>
