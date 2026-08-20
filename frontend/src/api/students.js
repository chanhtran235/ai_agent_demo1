import axios from 'axios'

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/'
})

export const getStudents = (params) => client.get('/students', { params })
export const searchStudents = (name, params) => client.get('/students/search', { params: { name, ...params } })
export const createStudent = (student) => client.post('/students', student)
export const updateStudent = (id, student) => client.put(`/students/${id}`, student)
export const deleteStudent = (id) => client.delete(`/students/${id}`)
