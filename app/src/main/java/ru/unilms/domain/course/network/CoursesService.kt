package ru.unilms.domain.course.network

import ru.unilms.domain.common.model.ErrorResponse
import ru.unilms.domain.common.network.Response
import ru.unilms.domain.course.model.Block
import ru.unilms.domain.course.model.Course
import ru.unilms.domain.course.model.CourseContent
import ru.unilms.domain.course.model.CourseTutor
import ru.unilms.domain.course.model.CreateCourseRequest
import ru.unilms.domain.course.model.CreateFileRequest
import ru.unilms.domain.course.model.FileResponse
import ru.unilms.domain.course.model.TextContentInfo
import ru.unilms.domain.course.util.enums.CourseType
import ru.unilms.domain.file.model.FileContentInfo
import ru.unilms.domain.journal.model.JournalDto
import ru.unilms.domain.manage.model.Group
import ru.unilms.domain.task.model.TaskInfo
import java.util.UUID

interface CoursesService {
    suspend fun getEnrolled(type: CourseType): Response<List<Course>, ErrorResponse>
    suspend fun getOwned(): Response<List<CourseTutor>, ErrorResponse>
    suspend fun getCourseContents(courseId: UUID): Response<CourseContent, ErrorResponse>
    suspend fun getTextContent(textId: UUID): Response<ByteArray, ErrorResponse>
    suspend fun getTextContentInfo(textId: UUID): Response<TextContentInfo, ErrorResponse>
    suspend fun getFileContentInfo(fileId: UUID): Response<FileContentInfo, ErrorResponse>
    suspend fun getTaskInfo(taskId: UUID): Response<TaskInfo, ErrorResponse>
    suspend fun getJournal(courseId: UUID): Response<JournalDto, ErrorResponse>
    suspend fun getBlocks(): Response<List<Block>, ErrorResponse>
    suspend fun getGroups(): Response<List<Group>, ErrorResponse>
    suspend fun createCourse(request: CreateCourseRequest): Response<Course, ErrorResponse>
    suspend fun createFile(request: CreateFileRequest): Response<FileResponse, ErrorResponse>
}