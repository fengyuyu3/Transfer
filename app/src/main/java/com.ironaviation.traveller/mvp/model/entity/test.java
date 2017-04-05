package com.ironaviation.traveller.mvp.model.entity;

import java.util.List;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/4/5 9:48
 * 修改人：
 * 修改时间：2017/4/5 9:48
 * 修改备注：
 */

public class test {


    /**
     * Data : {"Info":{"From":"string","To":"string","FlightNo":"string","Company":"string","Date":"2017-04-05T01:44:42.413Z","Rate":0},"List":[{"TakeOff":"string","Arrive":"string","TakeOffTime":"2017-04-05T01:44:42.413Z","RealityTakeOffTime":"2017-04-05T01:44:42.413Z","ArriveTime":"2017-04-05T01:44:42.413Z","RealityArriveTime":"2017-04-05T01:44:42.413Z","State":"string","StateId":0,"Gate":"string","CheckInCounter":"string","PackageGate":"string"}]}
     * Errors : [{"Field":"string","ErrorMessages":["string"]}]
     * Url : string
     * ServerStatus : Fail
     * Status : Unauthorized
     * Message : string
     */

    private DataBean Data;
    private String Url;
    private String ServerStatus;
    private String Status;
    private String Message;
    private List<ErrorsBean> Errors;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getServerStatus() {
        return ServerStatus;
    }

    public void setServerStatus(String ServerStatus) {
        this.ServerStatus = ServerStatus;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<ErrorsBean> getErrors() {
        return Errors;
    }

    public void setErrors(List<ErrorsBean> Errors) {
        this.Errors = Errors;
    }

    public static class DataBean {
        /**
         * Info : {"From":"string","To":"string","FlightNo":"string","Company":"string","Date":"2017-04-05T01:44:42.413Z","Rate":0}
         * List : [{"TakeOff":"string","Arrive":"string","TakeOffTime":"2017-04-05T01:44:42.413Z","RealityTakeOffTime":"2017-04-05T01:44:42.413Z","ArriveTime":"2017-04-05T01:44:42.413Z","RealityArriveTime":"2017-04-05T01:44:42.413Z","State":"string","StateId":0,"Gate":"string","CheckInCounter":"string","PackageGate":"string"}]
         */

        private InfoBean Info;
        private java.util.List<ListBean> List;

        public InfoBean getInfo() {
            return Info;
        }

        public void setInfo(InfoBean Info) {
            this.Info = Info;
        }

        public List<ListBean> getList() {
            return List;
        }

        public void setList(List<ListBean> List) {
            this.List = List;
        }

        public static class InfoBean {
            /**
             * From : string
             * To : string
             * FlightNo : string
             * Company : string
             * Date : 2017-04-05T01:44:42.413Z
             * Rate : 0
             */

            private String From;
            private String To;
            private String FlightNo;
            private String Company;
            private String Date;
            private int Rate;

            public String getFrom() {
                return From;
            }

            public void setFrom(String From) {
                this.From = From;
            }

            public String getTo() {
                return To;
            }

            public void setTo(String To) {
                this.To = To;
            }

            public String getFlightNo() {
                return FlightNo;
            }

            public void setFlightNo(String FlightNo) {
                this.FlightNo = FlightNo;
            }

            public String getCompany() {
                return Company;
            }

            public void setCompany(String Company) {
                this.Company = Company;
            }

            public String getDate() {
                return Date;
            }

            public void setDate(String Date) {
                this.Date = Date;
            }

            public int getRate() {
                return Rate;
            }

            public void setRate(int Rate) {
                this.Rate = Rate;
            }
        }

        public static class ListBean {
            /**
             * TakeOff : string
             * Arrive : string
             * TakeOffTime : 2017-04-05T01:44:42.413Z
             * RealityTakeOffTime : 2017-04-05T01:44:42.413Z
             * ArriveTime : 2017-04-05T01:44:42.413Z
             * RealityArriveTime : 2017-04-05T01:44:42.413Z
             * State : string
             * StateId : 0
             * Gate : string
             * CheckInCounter : string
             * PackageGate : string
             */

            private String TakeOff;
            private String Arrive;
            private String TakeOffTime;
            private String RealityTakeOffTime;
            private String ArriveTime;
            private String RealityArriveTime;
            private String State;
            private int StateId;
            private String Gate;
            private String CheckInCounter;
            private String PackageGate;

            public String getTakeOff() {
                return TakeOff;
            }

            public void setTakeOff(String TakeOff) {
                this.TakeOff = TakeOff;
            }

            public String getArrive() {
                return Arrive;
            }

            public void setArrive(String Arrive) {
                this.Arrive = Arrive;
            }

            public String getTakeOffTime() {
                return TakeOffTime;
            }

            public void setTakeOffTime(String TakeOffTime) {
                this.TakeOffTime = TakeOffTime;
            }

            public String getRealityTakeOffTime() {
                return RealityTakeOffTime;
            }

            public void setRealityTakeOffTime(String RealityTakeOffTime) {
                this.RealityTakeOffTime = RealityTakeOffTime;
            }

            public String getArriveTime() {
                return ArriveTime;
            }

            public void setArriveTime(String ArriveTime) {
                this.ArriveTime = ArriveTime;
            }

            public String getRealityArriveTime() {
                return RealityArriveTime;
            }

            public void setRealityArriveTime(String RealityArriveTime) {
                this.RealityArriveTime = RealityArriveTime;
            }

            public String getState() {
                return State;
            }

            public void setState(String State) {
                this.State = State;
            }

            public int getStateId() {
                return StateId;
            }

            public void setStateId(int StateId) {
                this.StateId = StateId;
            }

            public String getGate() {
                return Gate;
            }

            public void setGate(String Gate) {
                this.Gate = Gate;
            }

            public String getCheckInCounter() {
                return CheckInCounter;
            }

            public void setCheckInCounter(String CheckInCounter) {
                this.CheckInCounter = CheckInCounter;
            }

            public String getPackageGate() {
                return PackageGate;
            }

            public void setPackageGate(String PackageGate) {
                this.PackageGate = PackageGate;
            }
        }
    }

    public static class ErrorsBean {
        /**
         * Field : string
         * ErrorMessages : ["string"]
         */

        private String Field;
        private List<String> ErrorMessages;

        public String getField() {
            return Field;
        }

        public void setField(String Field) {
            this.Field = Field;
        }

        public List<String> getErrorMessages() {
            return ErrorMessages;
        }

        public void setErrorMessages(List<String> ErrorMessages) {
            this.ErrorMessages = ErrorMessages;
        }
    }
}
