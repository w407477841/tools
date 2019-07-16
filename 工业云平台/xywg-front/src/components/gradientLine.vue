<template>
  <div id="gradientLine" ref="gradientLine"></div>
</template>

<script>
export default {
  props: ["dataSource", "xList", "color0", "color100", "title"],
  data() {
    return {
      myChart: null
    };
  },
  mounted() {
    console.log(this.color0, this.color100, this.xList, this.dataSource);
    this.myChart = this.echarts.init(this.$refs.gradientLine);
    let option = {
      grid: {
        //直角坐标系内绘图网格
        top: "12%",

        left: "3%",

        right: "3%",

        bottom: "3%",

        containLabel: true
      },
      axisPointer: {
        lineStyle: {
          color: "#007cff"
        }
      },
      tooltip: {
        trigger: "axis",
        formatter:
          "<div style='border-bottom:1px solid #e7e9ed;padding:8px;'>{b}</div><div style='padding:8px;'><span style='display:inline-block;width:6px;height:6px;background-color:#007cff;border-radius:100%;margin-right:5px;'></span>" +
          this.title +
          ": {c0}</div> ",
        backgroundColor: "rgba(255,255,255,1)",
        padding: 0,
        boxShadow: "10px 10px 5px #888888;",
        textStyle: {
          color: "#130a33"
        },
        extraCssText:
          "box-shadow: 0px 4px 8px 0px rgba(0, 0, 0, 0.2);border-radius:0;"
      },
      xAxis: {
        type: "category",
        data: this.xList,
        axisLabel: {
          show: true,
          textStyle: {
            color: "#585860"
          }
        },
        axisLine: {
          lineStyle: {
            color: "#d6d8dd"
          }
        }
      },
      yAxis: {
        type: "value",
        axisLabel: {
          textStyle: {
            color: "#98BBD0"
          }
        },
        splitLine: {
          show: true,
          lineStyle: {
            type: "dashed"
          }
        },
        axisLine: {
          lineStyle: {
            color: "transparent"
          }
        }
      },
      series: [
        {
          symbol: "none",
          data: this.dataSource,
          type: "line",
          smooth: false,
          itemStyle: {
            normal: {
              lineStyle: {
                color: this.color0
              }
            }
          },
          areaStyle: {
            color: {
              type: "linear",
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: this.color100 // 0% 处的颜色
                },
                {
                  offset: 1,
                  color: this.color100 // 100% 处的颜色
                }
              ],
              globalCoord: false // 缺省为 false
            }
          }
        }
      ]
    };
    this.myChart.setOption(option);
    addEventOnResize(() => {
      this.myChart.resize();
    });
  },
  watch: {
    dataSource() {
      let option = {
        grid: {
          //直角坐标系内绘图网格
          top: "12%",

          left: "3%",

          right: "3%",

          bottom: "3%",

          containLabel: true
        },
        axisPointer: {
          lineStyle: {
            color: "#007cff"
          }
        },
        tooltip: {
          trigger: "axis",
          formatter:
            "<div style='border-bottom:1px solid #e7e9ed;padding:8px;'>{b}</div><div style='padding:8px;'><span style='display:inline-block;width:6px;height:6px;background-color:#007cff;border-radius:100%;margin-right:5px;'></span>" +
            this.title +
            ": {c0}</div> ",
          backgroundColor: "rgba(255,255,255,1)",
          padding: 0,
          boxShadow: "10px 10px 5px #888888;",
          textStyle: {
            color: "#130a33"
          },
          extraCssText:
            "box-shadow: 0px 4px 8px 0px rgba(0, 0, 0, 0.2);border-radius:0;"
        },
        xAxis: {
          type: "category",
          data: this.xList,
          axisLabel: {
            show: true,
            textStyle: {
              color: "#585860"
            }
          },
          axisLine: {
            lineStyle: {
              color: "#d6d8dd"
            }
          }
        },
        yAxis: {
          type: "value",
          axisLabel: {
            textStyle: {
              color: "#98BBD0"
            }
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: "dashed"
            }
          },
          axisLine: {
            lineStyle: {
              color: "transparent"
            }
          }
        },
        series: [
          {
            symbol: "none",
            data: this.dataSource,
            type: "line",
            smooth: false,
            itemStyle: {
              normal: {
                lineStyle: {
                  color: this.color0
                }
              }
            },
            areaStyle: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: this.color100 // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: this.color100 // 100% 处的颜色
                  }
                ],
                globalCoord: false // 缺省为 false
              }
            }
          }
        ]
      };
      this.myChart.setOption(option);
      addEventOnResize(() => {
        this.myChart.resize();
      });
    }
  }
};
</script>
