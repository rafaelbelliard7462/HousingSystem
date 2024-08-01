import axios from "axios";
import TokenManager from "./TokenManager";

axios.defaults.baseURL = "http://localhost:8080";
const ChatAndMessageApi = {


    getChatsByHomeSeekerId : (homeSeekerId : number) => axios.get(`/chat/homeSeeker/${homeSeekerId}/chats`, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
    .then(response => response.data),
    getChatsByHomeownerId : (homeownerId : number) => axios.get(`/chat/homeowner/${homeownerId}/chats`, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
    .then(response => response.data),

    postChat : (newChat: any) => axios.post("/chat/createChat", newChat,{
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),

    postMessage : (newMessage: any) => axios.post("/chat/sendMessage", newMessage,{
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    

      

}

export default ChatAndMessageApi;



